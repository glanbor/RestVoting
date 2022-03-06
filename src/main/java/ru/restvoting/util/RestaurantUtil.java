package ru.restvoting.util;

import ru.restvoting.model.Restaurant;
import ru.restvoting.to.RestaurantTo;

import java.util.Collection;
import java.util.List;


public class RestaurantUtil {

    private RestaurantUtil() {
    }

    public static List<RestaurantTo> getTos(Collection<Restaurant> restaurants) {
        return restaurants.stream()
                .map(res -> createTo(res, res.getVoteList().size()))
                .toList();
    }

    private static RestaurantTo createTo(Restaurant restaurant, int votes) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getDishList(),
                restaurant.getMenuList(), restaurant.getVoteList(), votes);
    }
}
