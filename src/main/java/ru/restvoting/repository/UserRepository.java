package ru.restvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.restvoting.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
