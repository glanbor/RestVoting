package ru.restvoting.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.restvoting.model.Restaurant;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

}
