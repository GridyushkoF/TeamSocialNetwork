package ru.skillbox.authentication.repository.nosql;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.authentication.model.entity.nosql.EmailChangeRequest;

import java.util.Optional;

@Repository
public interface EmailChangeRequestRepository extends CrudRepository<EmailChangeRequest,String> {
    Optional<EmailChangeRequest> findByOldEmail(String oldEmail);
}