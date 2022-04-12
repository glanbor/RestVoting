package ru.restvoting.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.restvoting.model.Menu;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:restaurantId AND m.menuDate >=:startDate " +
            "AND m.menuDate <=:endDate ORDER BY m.menuDate DESC")
    List<Menu> getAllForRestaurant(@Param("restaurantId") int restaurantId,
                                   @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @EntityGraph(attributePaths = {"dishList"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.menuDate = :lunchDate")
    List<Menu> getAllByDate(@Param("lunchDate") LocalDate lunchDate);

    @Transactional
    @Modifying
    @Query("DELETE FROM Menu m WHERE m.id=:id AND m.restaurant.id=:restaurantId")
    int delete(@Param("id") int id, @Param("restaurantId") int restaurantId);

    @EntityGraph(attributePaths = {"dishList"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.id= :id AND m.restaurant.id = :restaurantId")
    Menu getWithDishes(@Param("id") int id, @Param("restaurantId") int restaurantId);
}
