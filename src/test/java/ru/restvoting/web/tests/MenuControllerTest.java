package ru.restvoting.web.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.restvoting.model.Menu;
import ru.restvoting.repository.MenuRepository;
import ru.restvoting.util.exception.NotFoundException;
import ru.restvoting.web.AbstractControllerTest;
import ru.restvoting.web.data.MenuTestData;
import ru.restvoting.web.json.JsonUtil;
import ru.restvoting.web.menu.MenuController;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restvoting.util.ValidationUtil.checkNotFoundWithId;
import static ru.restvoting.web.TestUtil.userHttpBasic;
import static ru.restvoting.web.data.MenuTestData.*;
import static ru.restvoting.web.data.UserTestData.admin;
import static ru.restvoting.web.data.UserTestData.user;

class MenuControllerTest extends AbstractControllerTest {
    private static final String REST_URL =
            MenuController.REST_URL.replace("{restaurantId}", "100004") + '/';

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("startDate", "")
                .param("endDate", "")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_WITH_DISHES_MATCHER.contentJson(allUSAMenus));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menuUSA1));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getWithDishes() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU1_ID + "/with-dishes")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_WITH_DISHES_MATCHER.contentJson(menuUSA1));
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> checkNotFoundWithId(
                menuRepository.findById(NOT_FOUND).orElse(null), NOT_FOUND));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MENU1_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> checkNotFoundWithId(menuRepository.findById(MENU1_ID).orElse(null), MENU1_ID));
    }

    @Test
    void createWithLocation() throws Exception {
        Menu newMenu = MenuTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(JsonUtil.writeValue(newMenu)))
                .andExpect(status().isCreated());
        Menu created = MENU_WITH_DISHES_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenu.setId(newId);
        MENU_WITH_DISHES_MATCHER.assertMatch(created, newMenu);
        MENU_WITH_DISHES_MATCHER.assertMatch(menuRepository.getById(newId), newMenu);
    }

    @Test
    void update() throws Exception {
        Menu updated = MenuTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        MENU_WITH_DISHES_MATCHER.assertMatch(menuRepository.getById(MENU1_ID), updated);
    }
}