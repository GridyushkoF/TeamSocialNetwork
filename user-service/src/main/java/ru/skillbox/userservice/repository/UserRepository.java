package ru.skillbox.userservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skillbox.commonlib.dto.statistics.AgeCountDto;
import ru.skillbox.userservice.model.entity.User;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAll();

    Page<User> findAll(Pageable pageable);

    List<User> findAllByIsDeletedAndIdNot(Pageable page, boolean isDeleted, Long id);

    Optional<User> findByEmail(String email);
    int countByRegDateBetween(ZonedDateTime from, ZonedDateTime to);
    @Query("SELECT new ru.skillbox.commonlib.dto.statistics.AgeCountDto(" +
            "FLOOR(TIMESTAMPDIFF(YEAR, u.birthDate, CURRENT_DATE)), COUNT(u)) " +
            "FROM User u " +
            "WHERE u.isDeleted = false " +
            "GROUP BY FLOOR(TIMESTAMPDIFF(YEAR, u.birthDate, CURRENT_DATE)) " +
            "ORDER BY FLOOR(TIMESTAMPDIFF(YEAR, u.birthDate, CURRENT_DATE))")
    List<AgeCountDto> findAgeCountStatistics();

    @Query("from User u where day(u.birthDate) = day(CURRENT_DATE) and month(u.birthDate) = month(CURRENT_DATE)")
    List<User> findBirthdayUsers(LocalDateTime CURRENT_DATE);

}