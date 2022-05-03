package ru.restvoting.web.vote;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.restvoting.model.Vote;
import ru.restvoting.repository.VoteRepository;
import ru.restvoting.to.VoteTo;
import ru.restvoting.util.DateTimeUtil;
import ru.restvoting.util.VoteUtil;


import java.time.LocalDate;
import java.util.List;

import static ru.restvoting.util.ValidationUtil.*;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class VoteController {
    public static final String REST_URL = "/rest/admin/votes";

    private final VoteRepository voteRepository;

    @GetMapping()
    public List<VoteTo> getAll(@RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                               @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                               @RequestParam @Nullable Integer userId) {
        if (userId != null) {
            log.info("get votes with interval for user {}", userId);
            return VoteUtil.getTos(voteRepository.getAllByUser(userId, DateTimeUtil.setStartDate(startDate), DateTimeUtil.setEndDate(endDate)));
        } else {
            log.info("get all votes with interval");
            return VoteUtil.getTos(voteRepository.getAll(DateTimeUtil.setStartDate(startDate), DateTimeUtil.setEndDate(endDate)));
        }
    }

    @GetMapping("/{id}")
    public VoteTo get(@PathVariable int id) {
        log.info("get vote {}", id);
        return VoteUtil.createTo(checkNotFoundWithId(voteRepository.get(id).orElse(null), id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete vote {}", id);
        voteRepository.deleteExisted(id);
    }
}




