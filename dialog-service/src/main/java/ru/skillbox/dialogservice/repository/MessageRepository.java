package ru.skillbox.dialogservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.dialogservice.model.dto.MessageStatus;
import ru.skillbox.dialogservice.model.entity.Message;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>, PagingAndSortingRepository<Message,Long> {
    List<Message> findAllByAuthorIdAndRecipientIdAndStatus(Long authorId, Long recipientId, MessageStatus status);

    @Query("SELECT m FROM Message m WHERE (m.authorId = :member1 AND m.recipientId = :member2)" +
            " OR (m.authorId = :member2 AND m.recipientId = :member1) ORDER BY m.time DESC")
    Optional<Message> getLastMessageByMembers(@Param("member1") Long member1,
                                              @Param("member2") Long member2);
}
