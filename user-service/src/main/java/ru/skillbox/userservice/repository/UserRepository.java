package ru.skillbox.userservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skillbox.commondto.dto.statistics.AgeCountDto;
import ru.skillbox.userservice.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAll(Pageable pageable);

    List<User> findAllByIsDeleted(Pageable page, boolean isDeleted);

    Optional<User> findByEmail(String email);
    int countByRegDateBetween(LocalDateTime from,LocalDateTime to);
    @Query("SELECT new ru.skillbox.commondto.dto.statistics.AgeCountDto(" +
            "FLOOR(TIMESTAMPDIFF(YEAR, u.birthDate, CURRENT_DATE)), COUNT(u)) " +
            "FROM User u " +
            "WHERE u.isDeleted = false " +
            "GROUP BY FLOOR(TIMESTAMPDIFF(YEAR, u.birthDate, CURRENT_DATE)) " +
            "ORDER BY FLOOR(TIMESTAMPDIFF(YEAR, u.birthDate, CURRENT_DATE))")
    List<AgeCountDto> findAgeCountStatistics();
}