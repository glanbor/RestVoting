package ru.restvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.restvoting.model.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
}
