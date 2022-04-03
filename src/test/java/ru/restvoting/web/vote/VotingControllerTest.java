package ru.restvoting.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.restvoting.model.Vote;
import ru.restvoting.repository.VoteRepository;
import ru.restvoting.web.AbstractControllerTest;
import ru.restvoting.web.json.JsonUtil;
import ru.restvoting.web.user.AdminUserController;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restvoting.web.data.VoteTestData.*;

class VotingControllerTest extends AbstractControllerTest {
    private static final String REST_URL = VotingController.REST_URL + '/';

    @Autowired
    private VoteRepository voteRepository;

    @Test
    void createWithLocation() throws Exception {
        Vote newVote = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVote)))
                .andExpect(status().isCreated());

        Vote created = VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(voteRepository.getById(newId), newVote);
    }

    @Test
    void update() throws Exception {
        Vote updated = getUpdated();
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(REST_URL + VOTE1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        VOTE_MATCHER.assertMatch(voteRepository.getById(VOTE1_ID), updated);
    }
}