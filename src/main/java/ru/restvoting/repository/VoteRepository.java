package ru.restvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.restvoting.model.Vote;

public interface VoteRepository extends JpaRepository<Vote, Integer> {
}
