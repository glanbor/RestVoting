package ru.restvoting.web.menu;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restvoting.error.NotFoundException;
import ru.restvoting.model.Dish;
import ru.restvoting.model.Menu;
import ru.restvoting.model.Restaurant;
import ru.restvoting.repository.DishRepository;
import ru.restvoting.repository.MenuRepository;
import ru.restvoting.repository.RestaurantRepository;
import ru.restvoting.util.DateTimeUtil;
import ru.restvoting.util.ValidationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.restvoting.util.ValidationUtil.*;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController {
    public static final String REST_URL = "/rest/admin/restaurants/{restaurantId}/menus";

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    @GetMapping()
    public List<Menu> getAll(@PathVariable int restaurantId,
                             @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                             @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("get menus for restaurant {} with interval", restaurantId);
        return menuRepository.getAllForRestaurant(restaurantId, DateTimeUtil.setStartDate(startDate), DateTimeUtil.setEndDate(endDate));
    }

    @GetMapping("/{id}")
    public Menu get(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get menu {} for restaurant {} with dishes", id, restaurantId);
        Menu menu = menuRepository.getWithDishes(id, restaurantId);
        return checkNotFoundWithId(menu, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete menu {} for restaurant {}", id, restaurantId);
        menuRepository.deleteExisted(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<Menu> createWithLocation(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate,
                                                   @RequestParam List<Integer> dishIds,
                                                   @PathVariable int restaurantId) {
        log.info("create menu for restaurant {}", restaurantId);
        checkMenuDate(menuDate);
        List<Dish> dishList = dishIds.stream().map(dishId -> dishRepository.get(dishId, restaurantId)).toList();
        Restaurant restaurant = restaurantRepository.getById(restaurantId);
        Menu menu = new Menu(null, menuDate, restaurant, dishList);
        Menu created = menuRepository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

//    not good controller method for testing in swagger

//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Menu> createWithLocation(@Valid @RequestBody Menu menu, @PathVariable int restaurantId) {
//        log.info("create menu {} for restaurant {}", menu, restaurantId);
//        checkNew(menu);
//        Restaurant restaurant = restaurantRepository.getById(restaurantId);
//        menu.setRestaurant(restaurant);
//        menu.getDishList().forEach(dish -> dish.setRestaurant(restaurant));
//        Menu created = menuRepository.save(menu);
//        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path(REST_URL + "/{id}")
//                .buildAndExpand(restaurantId, created.getId()).toUri();
//        return ResponseEntity.created(uriOfNewResource).body(created);
//    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@PathVariable int id, @PathVariable int restaurantId,
                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate,
                       @RequestParam List<Integer> dishIds) {
        log.info("update menu with id={} for restaurant {}", id, restaurantId);
        checkMenuDate(menuDate);
        Menu menu = menuRepository.getWithDishes(id, restaurantId);
        ValidationUtil.assureIdConsistent(menu, id);
        List<Dish> dishList = dishIds.stream().map(dishId -> dishRepository.get(dishId, restaurantId)).toList();
        menu.setDishList(dishList);
        menu.setMenuDate(menuDate);
    }

//    not good controller method for testing in swagger

//    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void update(@Valid @RequestBody Menu menu, @PathVariable int id, @PathVariable int restaurantId) {
//        log.info("update menu {} with id={} for restaurant {}", menu, id, restaurantId);
//        ValidationUtil.assureIdConsistent(menu, id);
//        menu.setRestaurant(restaurantRepository.getById(restaurantId));
//        menuRepository.save(menu);
//    }
}
