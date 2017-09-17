package net.stuha.messages;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MessageRepository extends CrudRepository<Message, UUID> {

    List<Message> findFirst10ByConversationIdOrderByCreatedOnDesc(UUID conversationId);

    List<Message> findFirst1ByConversationIdAndId(UUID conversationId, UUID messageId);

    long countAllByConversationIdAndCreatedOnAfter(UUID conversationId, LocalDateTime createdOn);

    long countAllByConversationId(UUID conversationId);

    List<Message> findByConversationIdAndCreatedOnGreaterThanOrderByCreatedOnDesc(UUID conversationId, LocalDateTime createdOn);

    List<Message> findFirst10ByConversationIdAndCreatedOnLessThanOrderByCreatedOnDesc(UUID conversationId, LocalDateTime createdOn);
}
