package ru.restvoting.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.restvoting.model.Menu;
import ru.restvoting.model.User;
import ru.restvoting.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.userId=:id AND v.voteDate >=:startDate " +
            "AND v.voteDate <=:endDate ORDER BY v.voteDate DESC")
    List<Vote> getAllByUser(int id, LocalDate startDate, LocalDate endDate);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.userId=:id AND v.voteDate =:lunchDate")
    Vote getByUserForDate(int id, LocalDate lunchDate);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.voteDate >=:startDate " +
            "AND v.voteDate <=:endDate ORDER BY v.voteDate DESC")
    List<Vote> getAll(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.id=:id")
    int delete(@Param("id") int id);

}
