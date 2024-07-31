package ru.skillbox.userservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skillbox.userservice.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAll(Pageable pageable);

    List<User> findAllByIsDeleted(Pageable page, boolean isDeleted);

    Optional<User> findByEmail(String email);

    @Query("from User u where day(u.birthDate) = day(CURRENT_DATE) and month(u.birthDate) = month(CURRENT_DATE)")
    List<User> findBirthdayUsers(LocalDateTime CURRENT_DATE);

}