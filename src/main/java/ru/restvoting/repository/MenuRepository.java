package ru.restvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.restvoting.model.Menu;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
}
