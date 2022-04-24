package ru.restvoting.util;

import ru.restvoting.model.Vote;
import ru.restvoting.to.VoteTo;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class VoteUtil {

    public static List<VoteTo> getTos(Collection<Vote> votes) {
        return votes.stream()
                .map(vote -> createTo(vote))
                .sorted(Comparator.comparing(VoteTo::getVoteDate))
                .toList();
    }

    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getVoteDate(), vote.getRestaurant().getName());
    }
}
