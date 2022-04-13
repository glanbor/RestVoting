package ru.restvoting.web.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.restvoting.model.Restaurant;
import ru.restvoting.repository.RestaurantRepository;
import ru.restvoting.util.RestaurantUtil;
import ru.restvoting.util.exception.NotFoundException;
import ru.restvoting.web.AbstractControllerTest;
import ru.restvoting.web.restaurant.RestaurantController;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restvoting.util.ValidationUtil.checkNotFoundWithId;
import static ru.restvoting.web.TestUtil.userHttpBasic;
import static ru.restvoting.web.data.DishTestData.FR_DISH1_ID;
import static ru.restvoting.web.data.RestaurantTestData.*;
import static ru.restvoting.web.data.UserTestData.admin;
import static ru.restvoting.web.data.UserTestData.user;
import static ru.restvoting.web.data.VoteTestData.allVotes;
import static ru.restvoting.web.json.JsonUtil.writeValue;

class RestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = RestaurantController.REST_URL + '/';

    @Autowired
    private RestaurantRepository restaurantRepository;


    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("startDate", "")
                .param("endDate", "")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(RestaurantUtil.getTos(allRestaurants, allVotes)));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurantUSA));
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
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> checkNotFoundWithId(
                restaurantRepository.findById(NOT_FOUND).orElse(null), NOT_FOUND));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT1_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(restaurantRepository.findById(FR_DISH1_ID).isPresent());
    }


    @Test
    void create() throws Exception {
        Restaurant newRestaurant = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(writeValue(newRestaurant)))
                .andExpect(status().isCreated());
        Restaurant created = RESTAURANT_MATCHER.readFromJson(action);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.getById(newId),newRestaurant);
    }

    @Test
    void update() throws Exception {
        Restaurant updated = getUpdated();
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.getById(RESTAURANT1_ID), getUpdated());
    }
}