package ru.restvoting.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.restvoting.model.Restaurant;

import ru.restvoting.repository.RestaurantRepository;
import ru.restvoting.util.ValidationUtil;

import javax.validation.Valid;
import java.util.List;

import static ru.restvoting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping("/restvot/restaurants")
public class RestaurantController {
    private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @Cacheable("restaurants")
    public List<Restaurant> getAll() {
        log.info("get all restaurants");
        return restaurantRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("get restaurant {}", id);
        Restaurant restaurant = restaurantRepository.findById(id).orElse(null);
        return ResponseEntity.ok(restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value="reataurants", allEntries = true)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant {}", id);
        restaurantRepository.deleteById(id);
    }

    @PostMapping()
    @CacheEvict(value="reataurants", allEntries = true)
    public ResponseEntity<Restaurant> create(@RequestBody Restaurant restaurant) {
        log.info("create restaurant {}", restaurant);
        checkNew(restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        return ResponseEntity.ok(created);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value="reataurants", allEntries = true)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update restaurant {}", restaurant);
        ValidationUtil.assureIdConsistent(restaurant, id);
        restaurantRepository.save(restaurant);
    }
}
