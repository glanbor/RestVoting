package ru.restvoting.web;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.Profiles;

import ru.restvoting.model.Role;
import ru.restvoting.model.User;
import ru.restvoting.web.user.AdminUserController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ClassPathXmlApplicationContext appCtx =
                     new ClassPathXmlApplicationContext("spring/spring-app.xml")) {


            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
//            AdminUserController adminUserController = appCtx.getBean(AdminUserController.class);
//
//            adminUserController.createWithLocation(
//                    new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));
//            System.out.println();
//            mockAuthorize(user);
//            MealRestController mealController = appCtx.getBean(MealRestController.class);
//            List<MealTo> filteredMealsWithExcess =
//                    mealController.getBetween(
//                            LocalDate.of(2020, Month.JANUARY, 30), LocalTime.of(7, 0),
//                            LocalDate.of(2020, Month.JANUARY, 31), LocalTime.of(11, 0));
//            filteredMealsWithExcess.forEach(System.out::println);
//            System.out.println();
//            System.out.println(mealController.getBetween(null, null, null, null));
        }
    }
}
