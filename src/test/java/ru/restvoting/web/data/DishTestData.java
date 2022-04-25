package ru.restvoting.web.data;

import ru.restvoting.model.Dish;
import ru.restvoting.web.MatcherFactory;

import java.util.List;

import static ru.restvoting.model.AbstractBaseEntity.START_SEQ;
import static ru.restvoting.web.data.RestaurantTestData.*;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "restaurant");

    public static final int NOT_FOUND = 10;
    public static final int DISH1_ID = START_SEQ + 20;
    public static final int FR_DISH1_ID = DISH1_ID + 9;

    public static final Dish amDish1 = new Dish(DISH1_ID, "AmFood1", 10.99, restaurantUSA);
    public static final Dish amDish2 = new Dish(DISH1_ID+1, "AmFood2", 20.99, restaurantUSA);
    public static final Dish itDish1 = new Dish(DISH1_ID+2, "ItFood1", 10.50, restaurantItalia);
    public static final Dish itDish2 = new Dish(DISH1_ID+3, "ItFood2", 20.50, restaurantItalia);
    public static final Dish itDish3 = new Dish(DISH1_ID+4, "ItFood3", 30.50, restaurantItalia);
    public static final Dish ukDish1 = new Dish(DISH1_ID+5, "UkFood1", 10, restaurantUkraine);
    public static final Dish ukDish2 = new Dish(DISH1_ID+6, "UkFood2", 20, restaurantUkraine);
    public static final Dish ukDish3 = new Dish(DISH1_ID+7, "UkFood3", 30, restaurantUkraine);
    public static final Dish ukDish4= new Dish(DISH1_ID+8, "UkFood4", 40, restaurantUkraine);
    public static final Dish frDish1= new Dish(DISH1_ID+9, "FrFood1", 100, restaurantFrance);
    public static final Dish frDish2= new Dish(DISH1_ID+10, "FrFood2", 200, restaurantFrance);
    public static final Dish frDish3= new Dish(DISH1_ID+11, "FrFood3", 300, restaurantFrance);
    public static final Dish frDish4= new Dish(DISH1_ID+12, "FrFood4", 400, restaurantFrance);
    public static final Dish frDish5= new Dish(DISH1_ID+13, "FrFood5", 500, restaurantFrance);

    public static final List<Dish> allFrenchDishes = List.of(frDish1, frDish2, frDish3, frDish4, frDish5);

    public static final List<Dish> dishesForUSAMenu1 = List.of(amDish1);
    public static final List<Dish> dishesForUSAMenu2 = List.of(amDish2);
    public static final List<Dish> dishesForUSAMenu3 = List.of(amDish1, amDish2);
    public static final List<Dish> dishesForItalianMenu1 = List.of(itDish1, itDish2);
    public static final List<Dish> dishesForItalianMenu2 = List.of(itDish1, itDish3);
    public static final List<Dish> dishesForItalianMenu3 = List.of(itDish1, itDish2, itDish3);
    public static final List<Dish> dishesForUkrainianMenu1 = List.of(ukDish1, ukDish2, ukDish3);
    public static final List<Dish> dishesForUkrainianMenu2 = List.of(ukDish2, ukDish3, ukDish4);
    public static final List<Dish> dishesForUkrainianMenu3 = List.of(ukDish1, ukDish2, ukDish4);
    public static final List<Dish> dishesForFrenchMenu1 = List.of(frDish1, frDish2, frDish3);
    public static final List<Dish> dishesForFrenchMenu2 = List.of(frDish1, frDish2, frDish4);
    public static final List<Dish> dishesForFrenchMenu3 = List.of(frDish1, frDish2, frDish3, frDish5);

    public static Dish getNew() {
        return new Dish(null, "Created Dish", 99, restaurantFrance);
    }
    public static Dish getUpdated(){
        return new Dish(FR_DISH1_ID, "Updated Dish", 99, restaurantFrance);}
}
