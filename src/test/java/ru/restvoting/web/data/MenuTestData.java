package ru.restvoting.web.data;

import ru.restvoting.model.Menu;
import ru.restvoting.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static ru.restvoting.model.AbstractBaseEntity.START_SEQ;
import static ru.restvoting.web.data.DishTestData.*;
import static ru.restvoting.web.data.RestaurantTestData.*;

public class MenuTestData {
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "restaurant", "dishList");
    public static final MatcherFactory.Matcher<Menu> MENU_WITH_DISHES_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "restaurant", "dishList.restaurant");

//    public static final MatcherFactory.Matcher<Menu> MENU_WITH_DISHES_MATCHER =
//            MatcherFactory.usingAssertions(Menu.class,
//                    (a, e) -> assertThat(a).usingRecursiveComparison().
//                            ignoringFields("restaurant", "dishList.restaurant").isEqualTo(e),
//                    (a, e) -> {
//                        throw new UnsupportedOperationException();
//                    });

    public static final int NOT_FOUND = 10;
    public static final int MENU1_ID = START_SEQ + 8;

    public static final Menu menuUSA1 =
            new Menu(MENU1_ID, LocalDate.of(2022, 03, 01), restaurantUSA, dishesForUSAMenu1);
    public static final Menu menuUSA2 =
            new Menu(MENU1_ID + 1, LocalDate.of(2022, 03, 02), restaurantUSA, dishesForUSAMenu2);
    public static final Menu menuUSA3 =
            new Menu(MENU1_ID + 2, LocalDate.now(), restaurantUSA, dishesForUSAMenu3);
    public static final Menu menuItalia1 =
            new Menu(MENU1_ID + 3, LocalDate.of(2022, 03, 01), restaurantItalia, dishesForItalianMenu1);
    public static final Menu menuItalia2 =
            new Menu(MENU1_ID + 4, LocalDate.of(2022, 03, 02), restaurantItalia, dishesForItalianMenu2);
    public static final Menu menuItalia3 =
            new Menu(MENU1_ID + 5, LocalDate.now(), restaurantItalia, dishesForItalianMenu3);
    public static final Menu menuUkraine1 =
            new Menu(MENU1_ID + 6, LocalDate.of(2022, 03, 01), restaurantUkraine, dishesForUkrainianMenu1);
    public static final Menu menuUkraine2 =
            new Menu(MENU1_ID + 7, LocalDate.of(2022, 03, 02), restaurantUkraine, dishesForUkrainianMenu2);
    public static final Menu menuUkraine3 =
            new Menu(MENU1_ID + 8, LocalDate.now(), restaurantUkraine, dishesForUkrainianMenu3);
    public static final Menu menuFrance1 =
            new Menu(MENU1_ID + 9, LocalDate.of(2022, 03, 01), restaurantFrance, dishesForFrenchMenu1);
    public static final Menu menuFrance2 =
            new Menu(MENU1_ID + 10, LocalDate.of(2022, 03, 02), restaurantFrance, dishesForFrenchMenu2);
    public static final Menu menuFrance3 =
            new Menu(MENU1_ID + 11, LocalDate.now(), restaurantFrance, dishesForFrenchMenu3);

    public static final List<Menu> allUSAMenus = List.of(menuUSA3, menuUSA2, menuUSA1);

    public static final List<Menu> allTodayMenu = List.of(menuUSA3, menuItalia3, menuUkraine3, menuFrance3);

    public static Menu getNew() {
        return new Menu(null, LocalDate.now(), restaurantUSA, dishesForUSAMenu3);
    }

    public static Menu getUpdated() {
        return new Menu(MENU1_ID, LocalDate.now(), restaurantUSA, dishesForUSAMenu3);
    }
}
