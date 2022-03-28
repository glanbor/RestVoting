package ru.restvoting.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restvoting.model.Restaurant;

import ru.restvoting.repository.RestaurantRepository;
import ru.restvoting.to.RestaurantTo;
import ru.restvoting.util.RestaurantUtil;
import ru.restvoting.util.ValidationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.restvoting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    public static final String REST_URL = "/rest/admin/restaurants";
    private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }


    @GetMapping()
    @Cacheable("restaurants")
    public List<RestaurantTo> getAll() {
        log.info("get all restaurants");
        return RestaurantUtil.getTos(restaurantRepository.findAll(Sort.by("name")));
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get restaurant {}", id);
        Restaurant restaurant = ValidationUtil.checkNotFoundWithId(
                restaurantRepository.findById(id).orElse(null), id);
        return restaurant;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value="restaurants", allEntries = true)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant {}", id);
        ValidationUtil.checkNotFoundWithId(restaurantRepository.delete(id), id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value="restaurants", allEntries = true)
    public ResponseEntity<Restaurant> create(@RequestBody Restaurant restaurant) {
        log.info("create restaurant {}", restaurant);
        checkNew(restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value="restaurants", allEntries = true)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update restaurant {}", restaurant);
        ValidationUtil.assureIdConsistent(restaurant, id);
        restaurantRepository.save(restaurant);
    }
}
