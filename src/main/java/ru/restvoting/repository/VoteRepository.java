package ru.restvoting.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.restvoting.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.userId=:id AND v.voteDate >=:startDate " +
            "AND v.voteDate <=:endDate ORDER BY v.voteDate DESC")
    List<Vote> getAllByUser(@Param("id") int id, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.userId=:id AND v.voteDate =:lunchDate")
    Vote getByUserForDate(@Param("id") int id, @Param("lunchDate") LocalDate lunchDate);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.voteDate >=:startDate " +
            "AND v.voteDate <=:endDate ORDER BY v.voteDate DESC")
    List<Vote> getAll(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.id = ?1")
    Optional<Vote> get(int id);



}
