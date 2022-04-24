package ru.restvoting.web.vote;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restvoting.AuthorizedUser;
import ru.restvoting.model.Menu;
import ru.restvoting.model.Vote;
import ru.restvoting.repository.MenuRepository;
import ru.restvoting.repository.RestaurantRepository;
import ru.restvoting.repository.VoteRepository;
import ru.restvoting.to.VoteTo;
import ru.restvoting.util.DateTimeUtil;
import ru.restvoting.util.VoteUtil;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.restvoting.util.ValidationUtil.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = VotingController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VotingController {
    public static final String REST_URL = "/rest/voting";

    private static final Logger log = LoggerFactory.getLogger(VoteController.class);

    private final VoteRepository voteRepository;
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping()
    @Cacheable("todayMenus")
    public List<Menu> getAllMenusForToday() {
        log.info("get all Menus for today");
        List<Menu> menuList = menuRepository.getAllByDate(LocalDate.now());
        return menuList;
    }

    @GetMapping("/by-user")
    public VoteTo getByUser(@AuthenticationPrincipal AuthorizedUser authUser,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lunchDate) {
        log.info("get vote for user {} for date {}", authUser, lunchDate);
        Vote byUserForDate = voteRepository.getByUserForDate(authUser.getId(), lunchDate);
        return VoteUtil.createTo(byUserForDate);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@RequestParam int restaurantId,
                                                   @AuthenticationPrincipal AuthorizedUser authUser) {
        int userId = authUser.getId();
        log.info("create vote for userId {}", userId);
        Vote newVote = new Vote(null, LocalDate.now(), userId, restaurantRepository.getById(restaurantId));
        validateVote(newVote);
        Vote created = voteRepository.save(newVote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Vote vote, @PathVariable int id,
                       @RequestParam int restaurantId,
                       @AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("update vote {} with id={}", vote, id);
        assureIdConsistent(vote, id);
        validateVote(vote);
        vote.setRestaurant(restaurantRepository.getById(restaurantId));
        voteRepository.save(vote);
    }

    @GetMapping("/by-date")
    public List<VoteTo> getAllByDate(
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lunchDate) {
        log.info("get all votes for date {}", lunchDate);
        return VoteUtil.getTos(voteRepository.getAll(DateTimeUtil.setStartDate(lunchDate), DateTimeUtil.setEndDate(lunchDate)));
    }
}

