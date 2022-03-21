package ru.restvoting.web.dish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.restvoting.model.Dish;
import ru.restvoting.repository.DishRepository;
import ru.restvoting.repository.RestaurantRepository;
import ru.restvoting.util.ValidationUtil;

import javax.validation.Valid;
import java.util.List;


import static ru.restvoting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping("/restvoting/admin/restaurants/{restaurantId}/dishes")
public class DishController {
    private static final Logger log = LoggerFactory.getLogger(DishController.class);

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public DishController(DishRepository dishRepository, RestaurantRepository restaurantRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Dish> getAll(@PathVariable int restaurantId) {
        log.info("getAll Dishes for restaurant {}", restaurantId);
        return dishRepository.getAll(restaurantId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Dish get(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get dish {} for restaurant {}", id, restaurantId);
        Dish dish = dishRepository.findById(id)
                .filter(d -> d.getRestaurant().getId() == restaurantId).orElse(null);
        return dish;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete dish {} for restaurant {}", id, restaurantId);
        ValidationUtil.checkNotFoundWithId(dishRepository.delete(id, restaurantId), id);
    }

    @PostMapping()
    public ResponseEntity<Dish> create(@RequestBody Dish dish, @PathVariable int restaurantId) {
        log.info("create dish {} for restaurant {}", dish, restaurantId);
        checkNew(dish);
        dish.setRestaurant(restaurantRepository.getById(restaurantId));
        Dish created = dishRepository.save(dish);
        return ResponseEntity.ok(created);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id, @PathVariable int restaurantId) {
        log.info("update {} with id={} for restaurant {}", dish, id, restaurantId);
        ValidationUtil.assureIdConsistent(dish, id);
        dish.setRestaurant(restaurantRepository.getById(restaurantId));
        dishRepository.save(dish);
    }
}
