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
import ru.restvoting.model.Vote;
import ru.restvoting.repository.VoteRepository;
import ru.restvoting.util.DateTimeUtil;
import ru.restvoting.util.ValidationUtil;
import ru.restvoting.util.VoteUtil;
import ru.restvoting.web.AbstractControllerTest;
import ru.restvoting.web.data.VoteTestData;
import ru.restvoting.util.JsonUtil;
import ru.restvoting.web.vote.ProfileVotingController;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restvoting.web.GlobalExceptionHandler.EXCEPTION_DUPLICATE_VOTE;
import static ru.restvoting.web.data.MenuTestData.MENU_WITH_DISHES_MATCHER;
import static ru.restvoting.web.data.MenuTestData.allTodayMenu;
import static ru.restvoting.web.data.RestaurantTestData.RESTAURANT_FR_ID;
import static ru.restvoting.web.data.UserTestData.*;
import static ru.restvoting.web.data.VoteTestData.*;

class ProfileVotingControllerTest extends AbstractControllerTest {
    private static final String REST_URL = ProfileVotingController.REST_URL + '/';

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllMenusForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_WITH_DISHES_MATCHER.contentJson(allTodayMenu));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getById() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-user")
                .param("lunchDate", String.valueOf(LocalDate.now())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(VoteUtil.createTo(vote3)));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-user-" + USER_ID)
                .param("lunchDate", String.valueOf(LocalDate.now())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER2_MAIL)
    void createWithLocation() throws Exception {
        Vote newVote = VoteTestData.getNew();
        try (MockedStatic<DateTimeUtil> dateTimeUtilMockedStatic = mockStatic(DateTimeUtil.class)) {
            dateTimeUtilMockedStatic.when(DateTimeUtil::getLocalTime)
                    .thenReturn(ValidationUtil.VOTING_DEADLINE.minus(1, ChronoUnit.HOURS));

            ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                    .param("restaurantId", String.valueOf(RESTAURANT_FR_ID))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isCreated());

            Vote created = VOTE_MATCHER.readFromJson(action);
            int newId = created.id();
            newVote.setId(newId);
            VOTE_MATCHER.assertMatch(created, newVote);
            VOTE_MATCHER.assertMatch(voteRepository.getById(newId), newVote);
        }
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        try (MockedStatic<DateTimeUtil> dateTimeUtilMockedStatic = mockStatic(DateTimeUtil.class)) {
            dateTimeUtilMockedStatic.when(DateTimeUtil::getLocalTime)
                    .thenReturn(ValidationUtil.VOTING_DEADLINE.minus(1, ChronoUnit.HOURS));
            perform(MockMvcRequestBuilders.post(REST_URL)
                    .param("restaurantId", String.valueOf(RESTAURANT_FR_ID))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string(containsString(EXCEPTION_DUPLICATE_VOTE)));
        }
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        Vote updated = VoteTestData.getUpdated();
        try (MockedStatic<DateTimeUtil> dateTimeUtilMockedStatic = mockStatic(DateTimeUtil.class)) {
            dateTimeUtilMockedStatic.when(DateTimeUtil::getLocalTime)
                    .thenReturn(ValidationUtil.VOTING_DEADLINE.minus(1, ChronoUnit.HOURS));

            perform(MockMvcRequestBuilders.put(REST_URL + TODAY_VOTE1_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("restaurantId", String.valueOf(RESTAURANT_FR_ID))
                    .content(JsonUtil.writeValue(updated)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
            VOTE_MATCHER.assertMatch(voteRepository.getById(TODAY_VOTE1_ID), updated);
        }
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateInvalid() throws Exception {
        Vote updated = VoteTestData.getUpdated();
        try (MockedStatic<DateTimeUtil> dateTimeUtilMockedStatic = mockStatic(DateTimeUtil.class)) {
            dateTimeUtilMockedStatic.when(DateTimeUtil::getLocalTime)
                    .thenReturn(ValidationUtil.VOTING_DEADLINE.plus(1, ChronoUnit.HOURS));

            perform(MockMvcRequestBuilders.put(REST_URL + TODAY_VOTE1_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("restaurantId", String.valueOf(RESTAURANT_FR_ID))
                    .content(JsonUtil.writeValue(updated)))
                    .andDo(print())
                    .andExpect(status().isConflict());
        }
    }


}