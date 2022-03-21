package ru.restvoting.web.menu;

import lombok.AllArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
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
import static ru.restvoting.util.ValidationUtil.checkNotFoundWithId;

@RestController
@AllArgsConstructor
@RequestMapping("/restvoting/admin/restaurants/{restaurantId}/menus")
public class MenuController {
    private static final Logger log = LoggerFactory.getLogger(MenuController.class);

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @Cacheable("menus")
    public List<Menu> getAll(@PathVariable int restaurantId,
                             @RequestParam @Nullable LocalDate startDate,
                             @RequestParam @Nullable LocalDate endDate) {
        log.info("get menus for restaurant {} with interval", restaurantId);
        return menuRepository.getAll(restaurantId, DateTimeUtil.setStartDate(startDate), DateTimeUtil.setEndDate(endDate));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Menu get(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get menu {} for restaurant {}", id, restaurantId);
        Menu menu = menuRepository.findById(id).filter(m -> m.getRestaurant().getId() == restaurantId).orElse(null);
        return checkNotFoundWithId(menu, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value="menus", allEntries = true)
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete menu {} for restaurant {}", id, restaurantId);
        ValidationUtil.checkNotFoundWithId(menuRepository.delete(id, restaurantId), id);
    }

    @PostMapping()
    @CacheEvict(value="menus", allEntries = true)
    public ResponseEntity<Menu> create(@RequestBody Menu menu, @PathVariable int restaurantId) {
        log.info("create menu {} for restaurant {}", menu, restaurantId);
        checkNew(menu);
        menu.setRestaurant(restaurantRepository.getById(restaurantId));
        Menu created = menuRepository.save(menu);
        return ResponseEntity.ok(created);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value="menus", allEntries = true)
    public void update(@Valid @RequestBody Menu menu, @PathVariable int id, @PathVariable int restaurantId) {
        log.info("update menu {} with id={} for restaurant {}", menu, id, restaurantId);
        ValidationUtil.assureIdConsistent(menu, id);
        menu.setRestaurant(restaurantRepository.getById(restaurantId));
        menuRepository.save(menu);
    }
}
