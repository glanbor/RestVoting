package ru.restvoting.web.tests;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.restvoting.model.Menu;
import ru.restvoting.repository.MenuRepository;
import ru.restvoting.error.NotFoundException;
import ru.restvoting.util.DateTimeUtil;
import ru.restvoting.util.ValidationUtil;
import ru.restvoting.web.AbstractControllerTest;
import ru.restvoting.web.data.MenuTestData;
import ru.restvoting.util.JsonUtil;
import ru.restvoting.web.menu.MenuController;

import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restvoting.util.ValidationUtil.checkNotFoundWithId;
import static ru.restvoting.web.GlobalExceptionHandler.EXCEPTION_MENU_ERROR;
import static ru.restvoting.web.data.MenuTestData.*;
import static ru.restvoting.web.data.MenuTestData.NOT_FOUND;
import static ru.restvoting.web.data.RestaurantTestData.restaurantUSA;
import static ru.restvoting.web.data.UserTestData.*;

class MenuControllerTest extends AbstractControllerTest {
    private static final String REST_URL =
            MenuController.REST_URL.replace("{restaurantId}", "1") + '/';

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("startDate", "")
                .param("endDate", ""))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_WITH_DISHES_MATCHER.contentJson(allUSAMenus));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU1_ID))
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
    @WithUserDetails(value = USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getWithDishes() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_WITH_DISHES_MATCHER.contentJson(menuUSA1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MENU1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> checkNotFoundWithId(menuRepository.findById(MENU1_ID).orElse(null), MENU1_ID));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Menu newMenu = MenuTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("menuDate", newMenu.getMenuDate().toString())
                .param("dishIds", "1", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
        Menu created = MENU_WITH_DISHES_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenu.setId(newId);
        MENU_WITH_DISHES_MATCHER.assertMatch(created, newMenu);
        MENU_WITH_DISHES_MATCHER.assertMatch(menuRepository.getById(newId), newMenu);
    }

//    test for the commented method with Menu request body

//    @Test
//    @WithUserDetails(value = ADMIN_MAIL)
//    void createWithLocation() throws Exception {
//        Menu newMenu = MenuTestData.getNew();
//        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(JsonUtil.writeValue(newMenu)))
//                .andDo(print())
//                .andExpect(status().isCreated());
//        Menu created = MENU_WITH_DISHES_MATCHER.readFromJson(action);
//        int newId = created.id();
//        newMenu.setId(newId);
//        MENU_WITH_DISHES_MATCHER.assertMatch(created, newMenu);
//        MENU_WITH_DISHES_MATCHER.assertMatch(menuRepository.getById(newId), newMenu);
//    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        Menu duplicate = MenuTestData.getDuplicate();
        try (MockedStatic<DateTimeUtil> dateTimeUtilMockedStatic = mockStatic(DateTimeUtil.class)) {
            dateTimeUtilMockedStatic.when(DateTimeUtil::getLocalTime)
                    .thenReturn(ValidationUtil.VOTING_DEADLINE.minus(1, ChronoUnit.HOURS));
            perform(MockMvcRequestBuilders.post(REST_URL)
                    .param("menuDate", duplicate.getMenuDate().toString())
                    .param("dishIds", "1", "2")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string(containsString(EXCEPTION_MENU_ERROR)));
        }
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Menu updated = MenuTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + MENU1_ID)
                .param("menuDate", updated.getMenuDate().toString())
                .param("dishIds", "1", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        MENU_WITH_DISHES_MATCHER.assertMatch(menuRepository.getById(MENU1_ID), updated);
    }
}