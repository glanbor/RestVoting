package ru.restvoting.web.vote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.restvoting.model.User;
import ru.restvoting.model.Vote;
import ru.restvoting.repository.VoteRepository;
import ru.restvoting.util.DateTimeUtil;

import ru.restvoting.util.exception.AlreadyFoundException;


import java.time.LocalDate;
import java.util.List;

import static ru.restvoting.util.ValidationUtil.*;

@RestController
@RequestMapping("/restvot/votes")
public class VoteController {
    private static final Logger log = LoggerFactory.getLogger(VoteController.class);

    private final VoteRepository voteRepository;

    @Autowired
    public VoteController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Vote> getAll(@RequestParam @Nullable LocalDate startDate,
                             @RequestParam @Nullable LocalDate endDate,
                             @RequestParam @Nullable User user) {
        if (user != null) {
            log.info("get votes with interval for user {}", user);
            return voteRepository.getAllbyUser(user.id(), DateTimeUtil.setStartDate(startDate), DateTimeUtil.setStartDate(endDate));
        } else {
            log.info("get all votes with interval");
            return voteRepository.getAll(DateTimeUtil.setStartDate(startDate), DateTimeUtil.setStartDate(endDate));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vote> get(@PathVariable int id) {
        return ResponseEntity.of(voteRepository.findById(id));
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        voteRepository.deleteById(id);
    }

    @PostMapping()
    public ResponseEntity<Vote> create(@RequestBody Vote vote) {
        log.info("create {}", vote);
        checkNew(vote);
        checkVoteDateTime(vote);
        if (voteRepository.getAllbyUser(vote.getUserId(), LocalDate.now(), LocalDate.now()).size() == 0) {
            Vote created = voteRepository.save(vote);
            return ResponseEntity.ok(created);
        } else {
            throw new AlreadyFoundException("The vote fot User with id " + vote.getUserId() + " already exists");
        }
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Vote vote, @PathVariable int id) {
        log.info("update {} with id={}", vote, id);
        checkVoteDateTime(vote);
        assureIdConsistent(vote, id);
        voteRepository.save(vote);
    }
}




