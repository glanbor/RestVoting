package ru.restvoting.web.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.restvoting.model.User;
import ru.restvoting.repository.UserRepository;
import ru.restvoting.to.UserTo;
import ru.restvoting.util.UserUtil;
import ru.restvoting.web.AbstractControllerTest;
import ru.restvoting.util.JsonUtil;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restvoting.web.GlobalExceptionHandler.EXCEPTION_DUPLICATE_EMAIL;
import static ru.restvoting.web.data.UserTestData.*;
import static ru.restvoting.web.user.ProfileUserController.REST_URL;

class ProfileUserControllerTest extends AbstractControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
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
    @WithUserDetails(value = USER_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userRepository.findAll(), user2, admin, guest);
    }

    @Test
    void register() throws Exception {
        UserTo newTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword");
        User newUser = UserUtil.createNewFromTo(newTo);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        User created = USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userRepository.getById(newId), newUser);
    }

    @Test
    void registerInvalid() throws Exception {
        UserTo invalidNewTo = new UserTo(null, null, null, null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalidNewTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", "user@yandex.ru", "newPassword");
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userRepository.getById(USER_ID), UserUtil.updateFromTo(new User(user), updatedTo));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateInvalid() throws Exception {
        UserTo invalidUpdatedTo = new UserTo(null, "invalid", null, "newPassword");
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalidUpdatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        UserTo duplicate = new UserTo(null, "Duplicate", "admin@gmail.com", "newPassword");
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(duplicate)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(EXCEPTION_DUPLICATE_EMAIL)));
    }
}