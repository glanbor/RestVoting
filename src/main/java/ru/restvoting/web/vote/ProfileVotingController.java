package ru.restvoting.web.vote;

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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restvoting.error.IllegalRequestDataException;
import ru.restvoting.to.RestaurantTo;
import ru.restvoting.util.RestaurantUtil;
import ru.restvoting.web.AuthUser;
import ru.restvoting.model.Menu;
import ru.restvoting.model.Vote;
import ru.restvoting.repository.MenuRepository;
import ru.restvoting.repository.RestaurantRepository;
import ru.restvoting.repository.VoteRepository;
import ru.restvoting.to.VoteTo;
import ru.restvoting.util.DateTimeUtil;
import ru.restvoting.util.VoteUtil;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.restvoting.util.ValidationUtil.*;

@RestController
@CacheConfig(cacheNames = "voting")
@AllArgsConstructor
@Slf4j
@RequestMapping(value = ProfileVotingController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileVotingController {
    public static final String REST_URL = "/rest/voting";

    private final VoteRepository voteRepository;
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    @Operation(summary = "Get today's menus for voting")
    @GetMapping()
    @Cacheable("todayMenus")
    public List<Menu> getAllMenusForToday() {
        log.info("get all Menus for today");
        return menuRepository.getAllByDate(LocalDate.now());
    }

    @Operation(summary = "Get vote for authenticated use for date")
    @GetMapping("/by-user")
    @Cacheable
    public VoteTo getByUser(@AuthenticationPrincipal AuthUser authUser,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lunchDate) {
        log.info("get vote for user {} for date {}", authUser, lunchDate);
        Vote byUserForDate = voteRepository.getByUserForDate(authUser.id(), lunchDate);
        return VoteUtil.createTo(byUserForDate);
    }
    @Operation(summary = "Create vote for authenticated user")
    @CacheEvict(value = "voting", allEntries = true)
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<Vote> createWithLocation(@RequestParam int restaurantId,
                                                   @AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        log.info("create vote for userId {}", userId);
        Vote newVote = new Vote(null, LocalDate.now(), userId, restaurantRepository.getById(restaurantId));
        validateVote(newVote);
        Vote created = voteRepository.save(newVote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
    @Operation(summary = "Update vote for authenticated user")
    @CacheEvict(value = "voting", allEntries = true)
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@PathVariable int id, @RequestParam int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update vote with id={}", id);
        Vote vote = voteRepository.findById(id).orElseThrow(
                () -> new IllegalRequestDataException("There is no vote with this id"));
            validateVote(vote);
            vote.setRestaurant(restaurantRepository.getById(restaurantId));
            voteRepository.save(vote);
    }

}

