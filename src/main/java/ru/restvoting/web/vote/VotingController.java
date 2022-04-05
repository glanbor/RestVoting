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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restvoting.model.Menu;
import ru.restvoting.model.Vote;
import ru.restvoting.repository.MenuRepository;
import ru.restvoting.repository.VoteRepository;
import ru.restvoting.util.DateTimeUtil;
import ru.restvoting.util.ValidationUtil;
import ru.restvoting.util.exception.AlreadyFoundException;

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

    @GetMapping()
    @Cacheable("todayMenus")
    public List<Menu> getAllMenusForToday() {
        log.info("get all Menus for today");
        List<Menu> menuList = menuRepository.getByDate(LocalDate.now());
        return menuList;
    }

    @GetMapping("/by-user-{id}")
    public Vote getById(@PathVariable("id") int userId) {
        log.info("get today vote for user {}", userId);
        return voteRepository.getByUserForDate(userId, LocalDate.now());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@RequestBody Vote vote) {
        log.info("create {}", vote);
        checkNew(vote);
        validateVote(vote);
        if (voteRepository.getAllByUser(vote.getUserId(), LocalDate.now(), LocalDate.now()).size() == 0) {
            Vote created = voteRepository.save(vote);
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(REST_URL + "/{id}")
                    .buildAndExpand(created.getId()).toUri();
            return ResponseEntity.created(uriOfNewResource).body(created);
        } else {
            throw new AlreadyFoundException("The vote fot User with id " + vote.getUserId() + " already exists");
        }
    }

    @PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Vote vote, @PathVariable int id) {
        log.info("update vote {} with id={}", vote, id);
        ValidationUtil.assureIdConsistent(vote, id);
        ValidationUtil.validateVote(vote);
        voteRepository.save(vote);
    }

    @GetMapping("/by-date")
    public List<Vote> getAllByDate(@RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lunchDate) {
        log.info("get all votes for date");
        return voteRepository.getAll(DateTimeUtil.setStartDate(lunchDate), DateTimeUtil.setStartDate(lunchDate));
    }
}

