package net.stuha.messages;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ConversationRepository extends CrudRepository<Conversation, UUID> {
    @Query(value = "SELECT c.* FROM user_conversation uc LEFT JOIN conversation c ON uc.conversation_id = c.id WHERE uc.user_id = ?1 order by c.last_message_on DESC", nativeQuery = true)
    List<Conversation> findConversationsByUserId(UUID userId);

    @Query(value = "SELECT c.* FROM user_conversation uc LEFT JOIN conversation c ON uc.conversation_id = c.id WHERE uc.conversation_id = ?1 AND uc.user_id = ?2", nativeQuery = true)
    Conversation findConversationByIdAndUserId(UUID conversationId, UUID userId);

    @Modifying
    @Query("UPDATE Conversation c SET c.lastMessageOn = ?1 where c.id = ?2")
    void updateLastMessageOn(LocalDateTime lastMessageOn, UUID conversationId);
}
