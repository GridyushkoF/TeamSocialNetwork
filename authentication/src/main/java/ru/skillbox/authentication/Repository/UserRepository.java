package ru.skillbox.authentication.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.authentication.Entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
}
