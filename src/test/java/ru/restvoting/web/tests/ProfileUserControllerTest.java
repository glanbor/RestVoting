package ru.restvoting.web.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.restvoting.model.User;
import ru.restvoting.repository.UserRepository;
import ru.restvoting.to.UserTo;
import ru.restvoting.util.UserUtil;
import ru.restvoting.web.AbstractControllerTest;
import ru.restvoting.web.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restvoting.web.TestUtil.userHttpBasic;
import static ru.restvoting.web.data.UserTestData.*;
import static ru.restvoting.web.user.ProfileUserController.REST_URL;

class ProfileUserControllerTest extends AbstractControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userRepository.findAll(), user2, admin, guest);
    }

    @Test
    void update() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", "user@yandex.ru", "newPassword");
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userRepository.getById(USER_ID), UserUtil.updateFromTo(new User(user), updatedTo));
    }
}