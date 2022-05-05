package ru.restvoting.util;

import ru.restvoting.model.Restaurant;
import ru.restvoting.model.Vote;
import ru.restvoting.to.RestaurantTo;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class RestaurantUtil {

    public static List<RestaurantTo> getTos(Collection<Restaurant> restaurants, List<Vote> votes) {
        return restaurants.stream()
                .map(res -> createTo(res, votes.stream()
                        .filter(v -> v.getRestaurant().getId() == res.getId())
                        .collect(Collectors.toList())))
                .sorted(Comparator.comparingInt(RestaurantTo::getVotesAmount).reversed()
                        .thenComparing(RestaurantTo::getName))
                .toList();
    }

    private static RestaurantTo createTo(Restaurant restaurant, List<Vote> votes) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), votes, votes.size());
    }
}
