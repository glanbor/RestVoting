package ru.restvoting.web.data;

import ru.restvoting.model.Vote;
import ru.restvoting.to.VoteTo;
import ru.restvoting.util.VoteUtil;
import ru.restvoting.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static ru.restvoting.web.data.RestaurantTestData.*;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "restaurant");

    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(VoteTo.class, "");

    public static final int VOTE1_ID = 1;
    public static final int TODAY_VOTE1_ID = VOTE1_ID + 2;
    public static final int NOT_FOUND = 100;

    public static final Vote vote1 = new Vote(VOTE1_ID, LocalDate.of(2022, 3, 1), 1, restaurantUkraine);
    public static final Vote vote2 = new Vote(VOTE1_ID+1, LocalDate.of(2022, 3, 1), 2, restaurantUkraine);
    public static final Vote vote3 = new Vote(TODAY_VOTE1_ID, LocalDate.now(), 1, restaurantItalia);
    public static final Vote adminVote = new Vote(TODAY_VOTE1_ID+1, LocalDate.now(), 3, restaurantItalia);

    public static final List<Vote> allVotes = List.of(adminVote, vote3, vote2, vote1);

    public static final List<Vote> allTodayVotes = List.of(adminVote, vote3);

    public static final List<VoteTo> allVoteTos = VoteUtil.getTos(List.of(vote3, adminVote, vote1, vote2));
    public static final List<VoteTo> allTodayVoteTos = VoteUtil.getTos(List.of(vote3, adminVote));

    public static Vote getNew() {
        return new Vote(null, LocalDate.now(), 2, restaurantFrance);
    }

    public static Vote getUpdated() {
        return new Vote(TODAY_VOTE1_ID, LocalDate.now(), 1, restaurantFrance);
    }
}
