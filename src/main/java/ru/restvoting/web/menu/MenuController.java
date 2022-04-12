package ru.restvoting.web.menu;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restvoting.model.Menu;
import ru.restvoting.model.Restaurant;
import ru.restvoting.repository.MenuRepository;
import ru.restvoting.repository.RestaurantRepository;
import ru.restvoting.util.DateTimeUtil;
import ru.restvoting.util.ValidationUtil;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.restvoting.util.ValidationUtil.checkNew;
import static ru.restvoting.util.ValidationUtil.checkNotFoundWithId;

@RestController
@AllArgsConstructor
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController {
    public static final String REST_URL = "/rest/admin/restaurants/{restaurantId}/menus";
    private static final Logger log = LoggerFactory.getLogger(MenuController.class);

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;


    @GetMapping()
    @Cacheable("menus")
    public List<Menu> getAll(@PathVariable int restaurantId,
                             @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                             @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("get menus for restaurant {} with interval", restaurantId);
        return menuRepository.getAllForRestaurant(restaurantId, DateTimeUtil.setStartDate(startDate), DateTimeUtil.setEndDate(endDate));
    }

    @GetMapping("/{id}")
    public Menu get(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get menu {} for restaurant {}", id, restaurantId);
        Menu menu = menuRepository.findById(id).orElse(null);
        return checkNotFoundWithId(menu, id);
    }

    @GetMapping("/{id}/with-dishes")
    public Menu getWithMeals(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get menu {} for restaurant {} with dishes", id, restaurantId);
        Menu menu = menuRepository.getWithDishes(id, restaurantId);
        return checkNotFoundWithId(menu, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value="menus", allEntries = true)
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete menu {} for restaurant {}", id, restaurantId);
        ValidationUtil.checkNotFoundWithId(menuRepository.delete(id, restaurantId) !=0, id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value="menus", allEntries = true)
    public ResponseEntity<Menu> createWithLocation(@RequestBody Menu menu, @PathVariable int restaurantId) {
        log.info("create menu {} for restaurant {}", menu, restaurantId);
        checkNew(menu);
        Restaurant restaurant = restaurantRepository.getById(restaurantId);
        menu.setRestaurant(restaurant);
        menu.getDishList().forEach(dish -> dish.setRestaurant(restaurant));
        Menu created = menuRepository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value="menus", allEntries = true)
    public void update(@RequestBody Menu menu, @PathVariable int id, @PathVariable int restaurantId) {
        log.info("update menu {} with id={} for restaurant {}", menu, id, restaurantId);
        ValidationUtil.assureIdConsistent(menu, id);
        menu.setRestaurant(restaurantRepository.getById(restaurantId));
        menuRepository.save(menu);
    }
}
