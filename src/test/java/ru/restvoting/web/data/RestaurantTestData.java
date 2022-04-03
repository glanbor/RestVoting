package ru.restvoting.web.data;

import ru.restvoting.model.Restaurant;
import ru.restvoting.to.RestaurantTo;
import ru.restvoting.web.MatcherFactory;

import java.util.List;

import static ru.restvoting.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "votes", "dishes");
    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_MATCHER =
            MatcherFactory.usingEqualsComparator(RestaurantTo.class);

    public static final int NOT_FOUND = 10;
    public static final int RESTAURANT1_ID = START_SEQ + 4;

    public static final Restaurant restaurantUSA = new Restaurant(RESTAURANT1_ID, "USA");
    public static final Restaurant restaurantItalia = new Restaurant(RESTAURANT1_ID+1, "Italia");
    public static final Restaurant restaurantUkraine = new Restaurant(RESTAURANT1_ID+2, "Ukraine");
    public static final Restaurant restaurantFrance = new Restaurant(RESTAURANT1_ID+3, "France");

    public static final List<Restaurant> allRestaurants = List.of(restaurantUSA, restaurantItalia, restaurantUkraine, restaurantFrance);

    public static Restaurant getNew() {
        return new Restaurant(null, "CreatedRestaurant");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT1_ID, "UpdatedRestaurant");
    }
}
