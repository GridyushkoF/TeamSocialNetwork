package ru.skillbox.authentication.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skillbox.authentication.model.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query("from User u where lower(u.email) = lower( :email) and not u.isDeleted")
    Optional<User> findByEmail(String email);



    @Query("select case when count(u)> 0 then true else false end from User u where lower(u.email) = lower(:email) and not u.isDeleted")
    boolean existsByEmail(String email);

    @Query("select u from User u where u.id = :id and not u.isDeleted")
    Optional<User> findById(Long id);
}
