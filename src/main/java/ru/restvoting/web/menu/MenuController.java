package ru.restvoting.web.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.restvoting.model.Dish;
import ru.restvoting.model.Menu;
import ru.restvoting.repository.DishRepository;
import ru.restvoting.repository.MenuRepository;
import ru.restvoting.repository.RestaurantRepository;
import ru.restvoting.util.DateTimeUtil;
import ru.restvoting.util.ValidationUtil;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static ru.restvoting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping("/restvot/restaurants/{restaurantId}/menus")
public class MenuController {
    private static final Logger log = LoggerFactory.getLogger(MenuController.class);

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    @Autowired
    public MenuController(MenuRepository menuRepository, RestaurantRepository restaurantRepository, DishRepository dishRepository) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Menu> getAll(@PathVariable int restaurantId,
                             @RequestParam @Nullable LocalDate startDate,
                             @RequestParam @Nullable LocalDate endDate) {
        log.info("get menus for restaurant {} with interval", restaurantId);
        return menuRepository.getAll(restaurantId, DateTimeUtil.setStartDate(startDate), DateTimeUtil.setEndDate(endDate));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Menu> get(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get menu {} for restaurant {}", id, restaurantId);
        Menu menu = menuRepository.findById(id).filter(d -> d.getRestaurant().getId() == restaurantId).orElse(null);
        return ResponseEntity.ok(menu);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete menu {} for restaurant {}", id, restaurantId);
        menuRepository.delete(id, restaurantId);
    }

    @PostMapping()
    public ResponseEntity<Menu> create(@RequestBody Menu menu, @PathVariable int restaurantId) {
        log.info("create menu {} for restaurant {}", menu, restaurantId);
        checkNew(menu);
        menu.setRestaurant(restaurantRepository.getById(restaurantId));
        Menu created = menuRepository.save(menu);
        return ResponseEntity.ok(created);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Menu menu, @PathVariable int id, @PathVariable int restaurantId) {
        log.info("update menu {} with id={} for restaurant {}", menu, id, restaurantId);
        ValidationUtil.assureIdConsistent(menu, id);
        menu.setRestaurant(restaurantRepository.getById(restaurantId));
        menuRepository.save(menu);
    }
}
