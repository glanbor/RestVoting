package ru.restvoting.web.restaurant;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restvoting.model.Restaurant;

import ru.restvoting.model.Vote;
import ru.restvoting.repository.MenuRepository;
import ru.restvoting.repository.RestaurantRepository;
import ru.restvoting.repository.VoteRepository;
import ru.restvoting.to.RestaurantTo;
import ru.restvoting.util.DateTimeUtil;
import ru.restvoting.util.RestaurantUtil;
import ru.restvoting.util.ValidationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.restvoting.util.ValidationUtil.checkNew;

@RestController
@AllArgsConstructor
@Slf4j
@CacheConfig(cacheNames = "restaurants")
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantController {
    public static final String REST_URL = "/rest/admin/restaurants";

    private final RestaurantRepository restaurantRepository;
    private final VoteRepository voteRepository;
    private final MenuRepository menuRepository;

    @Operation(summary = "Get restaurants. Dates interval for counting votes amount may be specified")
    @GetMapping()
    @Cacheable
    public List<RestaurantTo> getAll(
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("get all restaurants with votes amount by voting dates interval");
        List<Vote> all = voteRepository.getAll(DateTimeUtil.setStartDate(startDate), DateTimeUtil.setEndDate(endDate));
        return RestaurantUtil.getTos(restaurantRepository.findAll(), all);
    }
    @Operation(summary = "Get restaurant by id")
    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get restaurant {}", id);
        return ValidationUtil.checkNotFoundWithId(restaurantRepository.findById(id).orElse(null), id);
    }
    @Operation(summary = "Delete restaurant by id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    public void delete(@PathVariable int id) {
        log.info("delete restaurant {}", id);
        menuRepository.deleteFromMenuBeforeDeletingTheRestaurant(id);
        restaurantRepository.deleteExisted(id);
    }

    @Operation(summary = "Create the restaurant. Test with name field")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value = "restaurants", allEntries = true)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create restaurant {}", restaurant);
        checkNew(restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
    @Operation(summary = "Update the restaurant. Test with only name field")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", allEntries = true)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update restaurant {}", restaurant);
        ValidationUtil.assureIdConsistent(restaurant, id);
        restaurantRepository.save(restaurant);
    }
}
