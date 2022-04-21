package ru.restvoting.web.tests;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.restvoting.model.Menu;
import ru.restvoting.model.Vote;
import ru.restvoting.repository.VoteRepository;
import ru.restvoting.util.DateTimeUtil;
import ru.restvoting.util.ValidationUtil;
import ru.restvoting.web.AbstractControllerTest;
import ru.restvoting.web.MatcherFactory;
import ru.restvoting.web.data.MenuTestData;
import ru.restvoting.web.data.VoteTestData;
import ru.restvoting.web.json.JsonUtil;
import ru.restvoting.web.vote.VotingController;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restvoting.util.exception.ErrorType.VALIDATION_ERROR;
import static ru.restvoting.web.ExceptionInfoHandler.EXCEPTION_DUPLICATE_MENU;
import static ru.restvoting.web.ExceptionInfoHandler.EXCEPTION_DUPLICATE_VOTE;
import static ru.restvoting.web.TestUtil.userHttpBasic;
import static ru.restvoting.web.data.MenuTestData.MENU_WITH_DISHES_MATCHER;
import static ru.restvoting.web.data.MenuTestData.allTodayMenu;
import static ru.restvoting.web.data.RestaurantTestData.restaurantUkraine;
import static ru.restvoting.web.data.UserTestData.*;
import static ru.restvoting.web.data.VoteTestData.*;

class VotingControllerTest extends AbstractControllerTest {
    private static final String REST_URL = VotingController.REST_URL + '/';

    @Autowired
    private VoteRepository voteRepository;

    @Test
    void getAllMenusForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_WITH_DISHES_MATCHER.contentJson(allTodayMenu));
    }

    @Test
    void getById() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-user-" + USER_ID)
                .param("lunchDate", String.valueOf(LocalDate.now()))
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote3));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-user-" + USER_ID)
                .param("lunchDate", String.valueOf(LocalDate.now())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createWithLocation() throws Exception {
        Vote newVote = VoteTestData.getNew();
        try (MockedStatic<DateTimeUtil> dateTimeUtilMockedStatic = mockStatic(DateTimeUtil.class)) {
            dateTimeUtilMockedStatic.when(DateTimeUtil::getLocalTime)
                    .thenReturn(ValidationUtil.VOTING_DEADLINE.minus(1, ChronoUnit.HOURS));

            ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(userHttpBasic(user))
                    .content(JsonUtil.writeValue(newVote)))
                    .andExpect(status().isCreated());

            Vote created = VOTE_MATCHER.readFromJson(action);
            int newId = created.id();
            newVote.setId(newId);
            VOTE_MATCHER.assertMatch(created, newVote);
            VOTE_MATCHER.assertMatch(voteRepository.getById(newId), newVote);
        }
    }

    @Test
    void createInvalidTime() throws Exception {
        Vote invalid = VoteTestData.getNew();
        try (MockedStatic<DateTimeUtil> dateTimeUtilMockedStatic = mockStatic(DateTimeUtil.class)) {
            dateTimeUtilMockedStatic.when(DateTimeUtil::getLocalTime)
                    .thenReturn(ValidationUtil.VOTING_DEADLINE.plus(1, ChronoUnit.HOURS));

            perform(MockMvcRequestBuilders.post(REST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(userHttpBasic(user))
                    .content(JsonUtil.writeValue(invalid)))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(errorType(VALIDATION_ERROR));
        }
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        Vote duplicate = VoteTestData.getNew();
        duplicate.setUserId(100000);
        try (MockedStatic<DateTimeUtil> dateTimeUtilMockedStatic = mockStatic(DateTimeUtil.class)) {
            dateTimeUtilMockedStatic.when(DateTimeUtil::getLocalTime)
                    .thenReturn(ValidationUtil.VOTING_DEADLINE.minus(1, ChronoUnit.HOURS));
            perform(MockMvcRequestBuilders.post(REST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(userHttpBasic(user))
                    .content(JsonUtil.writeValue(duplicate)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(errorType(VALIDATION_ERROR));
        }
    }

    @Test
    void update() throws Exception {
        Vote updated = VoteTestData.getUpdated();
        try (MockedStatic<DateTimeUtil> dateTimeUtilMockedStatic = mockStatic(DateTimeUtil.class)) {
            dateTimeUtilMockedStatic.when(DateTimeUtil::getLocalTime)
                    .thenReturn(ValidationUtil.VOTING_DEADLINE.minus(1, ChronoUnit.HOURS));

            perform(MockMvcRequestBuilders.put(REST_URL + TODAY_VOTE1_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(userHttpBasic(user))
                    .content(JsonUtil.writeValue(updated)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
            MatcherFactory.Matcher<Vote> voteMatcher = VOTE_MATCHER;
            voteMatcher.assertMatch(voteRepository.getById(TODAY_VOTE1_ID), updated);
        }
    }

    @Test
    void updateInvalid() throws Exception {
        Vote updated = VoteTestData.getUpdated();
        try (MockedStatic<DateTimeUtil> dateTimeUtilMockedStatic = mockStatic(DateTimeUtil.class)) {
            dateTimeUtilMockedStatic.when(DateTimeUtil::getLocalTime)
                    .thenReturn(ValidationUtil.VOTING_DEADLINE.plus(1, ChronoUnit.HOURS));

            perform(MockMvcRequestBuilders.put(REST_URL + TODAY_VOTE1_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(userHttpBasic(user))
                    .content(JsonUtil.writeValue(updated)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(errorType(VALIDATION_ERROR));
        }
    }

    @Test
    void getAllByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-date")
                .param("lunchDate", String.valueOf(LocalDate.now()))
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(allTodayVotes));
    }
}