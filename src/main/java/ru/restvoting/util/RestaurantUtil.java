package ru.restvoting.util;

import ru.restvoting.model.Restaurant;
import ru.restvoting.model.Vote;
import ru.restvoting.to.RestaurantTo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class RestaurantUtil {

    private RestaurantUtil() {
    }

    public static List<RestaurantTo> getTos(Collection<Restaurant> restaurants) {
        return restaurants.stream()
                .map(res -> createTo(res, res.getVoteList().stream().
                        filter(v -> v.getRestId() == res.getId())
                        .collect(Collectors.toList())))
                .toList();
    }

    private static RestaurantTo createTo(Restaurant restaurant, List<Vote> votes) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), votes, votes.size());
    }
}
