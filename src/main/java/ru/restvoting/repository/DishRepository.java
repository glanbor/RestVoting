package ru.restvoting.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.transaction.annotation.Transactional;
import ru.restvoting.error.NotFoundException;
import ru.restvoting.model.Dish;

import java.util.List;


@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId ORDER BY d.name")
    List<Dish> getAll(@Param("restaurantId") int restaurantId);

    default Dish get(int id, int restaurantId) {
        return this.findById(id)
                .filter(d -> d.getRestaurant().getId() == restaurantId).orElseThrow(
                        () -> new NotFoundException("No dish with this id in this restaurant"));
    }
}
