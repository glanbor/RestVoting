package ru.restvoting.web.TestData;

import ru.restvoting.model.Vote;
import ru.restvoting.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static ru.restvoting.model.AbstractBaseEntity.START_SEQ;
import static ru.restvoting.web.TestData.RestaurantTestData.*;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "restaurant");

    public static final int VOTE1_ID = START_SEQ + 34;
    public static final int VOTE_NOT_FOUND = 10;

    public static final Vote vote1 = new Vote(VOTE1_ID, LocalDate.of(2022, 03, 01), 100000, 100006);
    public static final Vote vote2 = new Vote(VOTE1_ID+1, LocalDate.of(2022, 03, 01), 100001, 100006);
    public static final Vote vote3 = new Vote(VOTE1_ID+2, LocalDate.now(), 100000, 100005);
    public static final Vote vote4 = new Vote(VOTE1_ID+3, LocalDate.now(), 100001, 100004);
    public static final Vote adminVote = new Vote(VOTE1_ID+4, LocalDate.now(), 100002, 100005);

    public static final List<Vote> votes = List.of(vote1, vote2, vote3, vote4, adminVote);

    public static Vote getNew() {
        return new Vote(VOTE1_ID+5, LocalDate.now(), 100000, 100007);
    }

    public static Vote getUpdated() {
        return new Vote(VOTE1_ID+3, LocalDate.now(), 100001, 100007);
    }
}
