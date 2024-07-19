package ru.skillbox.dialogservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.skillbox.dialogservice.model.entity.Dialog;

import java.util.Optional;

@Repository
public interface DialogRepository extends JpaRepository<Dialog, Long>, JpaSpecificationExecutor<Dialog> {
    Optional<Dialog> findByMember1IdAndMember2Id(Long member1Id, Long member2Id);

    default Optional<Dialog> findByMembersWithoutOrder(Long participant1Id, Long participant2Id) {
        return findByMember1IdAndMember2Id(participant1Id, participant2Id)
                .or(() -> findByMember1IdAndMember2Id(participant2Id, participant1Id));
    }
}
