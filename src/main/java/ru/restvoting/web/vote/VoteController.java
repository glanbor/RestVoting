package ru.restvoting.web.vote;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.restvoting.model.User;
import ru.restvoting.model.Vote;
import ru.restvoting.repository.VoteRepository;
import ru.restvoting.util.DateTimeUtil;

import ru.restvoting.util.ValidationUtil;
import ru.restvoting.util.exception.AlreadyFoundException;
import ru.restvoting.web.menu.MenuController;


import java.time.LocalDate;
import java.util.List;

import static ru.restvoting.util.ValidationUtil.*;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class VoteController {
    public static final String REST_URL = "/rest/admin/votes";
    private static final Logger log = LoggerFactory.getLogger(VoteController.class);

    private final VoteRepository voteRepository;

    @GetMapping()
    public List<Vote> getAll(@RequestParam @Nullable LocalDate startDate,
                             @RequestParam @Nullable LocalDate endDate,
                             @RequestParam @Nullable User user) {
        if (user != null) {
            log.info("get votes with interval for user {}", user);
            return voteRepository.getAllByUser(user.id(), DateTimeUtil.setStartDate(startDate), DateTimeUtil.setStartDate(endDate));
        } else {
            log.info("get all votes with interval");
            return voteRepository.getAll(DateTimeUtil.setStartDate(startDate), DateTimeUtil.setStartDate(endDate));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vote> get(@PathVariable int id) {
        return ValidationUtil.checkNotFoundWithId(ResponseEntity.of(voteRepository.findById(id)), id);
    }

    @DeleteMapping("/id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody Vote vote) {
        ValidationUtil.checkNotFoundWithId(voteRepository.delete(vote.id()), vote.id());
    }

}




