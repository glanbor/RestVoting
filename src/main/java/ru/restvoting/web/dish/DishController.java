package ru.restvoting.web.dish;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restvoting.model.BaseEntity;
import ru.restvoting.model.Dish;
import ru.restvoting.model.Menu;
import ru.restvoting.repository.DishRepository;
import ru.restvoting.repository.MenuRepository;
import ru.restvoting.repository.RestaurantRepository;
import ru.restvoting.util.DateTimeUtil;
import ru.restvoting.util.ValidationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.restvoting.util.ValidationUtil.checkNew;
import static ru.restvoting.util.ValidationUtil.checkNotFoundWithId;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {

    public static final String REST_URL = "/rest/admin/restaurants/{restaurantId}/dishes";

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    @GetMapping()
    public List<Dish> getAll(@PathVariable int restaurantId) {
        log.info("getAll Dishes for restaurant {}", restaurantId);
        return dishRepository.getAll(restaurantId);
    }

    @GetMapping("/{id}")
    public Dish get(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get dish {} for restaurant {}", id, restaurantId);
        return checkNotFoundWithId(dishRepository.get(id, restaurantId), id);
    }

    @Operation(summary = "Delete the dish for the restaurant. Menu with this dish will be deleted")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete dish {} for restaurant {}", id, restaurantId);
        List<Menu> allForRestaurant = menuRepository.getAllForRestaurant(restaurantId,
                DateTimeUtil.setStartDate(null), DateTimeUtil.setEndDate(null));
        Dish forDelete = get(id, restaurantId);
        List<Integer> menuIdsForDeleting = allForRestaurant.stream()
                .filter(menu -> menu.getDishList().contains(forDelete))
                .map(BaseEntity::getId)
                .toList();
        for (Integer menuId : menuIdsForDeleting) {
            menuRepository.deleteExisted(menuId);
        }
        dishRepository.deleteExisted(id);
    }

    @Operation(summary = "Create a dish for the restaurant. Test without Restaurant field")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish, @PathVariable int restaurantId) {
        log.info("create dish {} for restaurant {}", dish, restaurantId);
        checkNew(dish);
        dish.setRestaurant(restaurantRepository.getById(restaurantId));
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(summary = "Update the dish for the restaurant. Test without Restaurant field")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id, @PathVariable int restaurantId) {
        log.info("update {} with id={} for restaurant {}", dish, id, restaurantId);
        ValidationUtil.assureIdConsistent(dish, id);
        dish.setRestaurant(restaurantRepository.getById(restaurantId));
        dishRepository.save(dish);
    }
}
