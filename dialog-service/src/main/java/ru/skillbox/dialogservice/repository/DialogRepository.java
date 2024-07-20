package ru.skillbox.dialogservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.skillbox.dialogservice.model.entity.Dialog;


@Repository
public interface DialogRepository extends JpaRepository<Dialog, Long>, JpaSpecificationExecutor<Dialog> {
}
