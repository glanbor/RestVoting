package ru.restvoting.web.vote;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.restvoting.model.Vote;
import ru.restvoting.repository.VoteRepository;
import ru.restvoting.util.ValidationUtil;
import ru.restvoting.util.exception.AlreadyFoundException;

import java.time.LocalDate;

import static ru.restvoting.util.ValidationUtil.*;

@RestController
@AllArgsConstructor
@RequestMapping("/restvoting/voting")
public class VotingController {

    private static final Logger log = LoggerFactory.getLogger(VoteController.class);

    private final VoteRepository voteRepository;

    @DeleteMapping("/vote")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody Vote vote) {
        ValidationUtil.checkNotFoundWithId(voteRepository.delete(vote.id()), vote.id());
    }

    @PostMapping("/vote")
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

    @PutMapping(value = "/vote")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Vote vote, @PathVariable int id) {
        log.info("update {} with id={}", vote, id);
        checkVoteDateTime(vote);
        assureIdConsistent(vote, id);
        voteRepository.save(vote);
    }
}
