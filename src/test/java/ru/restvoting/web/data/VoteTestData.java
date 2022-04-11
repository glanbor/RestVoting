package ru.restvoting.web.data;

import ru.restvoting.model.Vote;
import ru.restvoting.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static ru.restvoting.model.AbstractBaseEntity.START_SEQ;
import static ru.restvoting.web.data.RestaurantTestData.*;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "");

    public static final int VOTE1_ID = START_SEQ + 34;
    public static final int VOTE_NOT_FOUND = 10;

    public static final Vote vote1 = new Vote(VOTE1_ID, LocalDate.of(2022, 03, 01), 100000, restaurantUkraine);
    public static final Vote vote2 = new Vote(VOTE1_ID+1, LocalDate.of(2022, 03, 01), 100001, restaurantUkraine);
    public static final Vote vote3 = new Vote(VOTE1_ID+2, LocalDate.now(), 100000, restaurantItalia);
    public static final Vote vote4 = new Vote(VOTE1_ID+3, LocalDate.now(), 100001, restaurantUSA);
    public static final Vote adminVote = new Vote(VOTE1_ID+4, LocalDate.now(), 100002, restaurantItalia);

    public static final List<Vote> allVotes = List.of(vote1, vote2, vote3, vote4, adminVote);

    public static final List<Vote> allTodayVotes = List.of(vote3, vote4, adminVote);

    public static Vote getNew() {
        return new Vote(VOTE1_ID+5, LocalDate.now(), 100000, restaurantFrance);
    }

    public static Vote getUpdated() {
        return new Vote(VOTE1_ID, LocalDate.now(), 100000, restaurantFrance);
    }
}