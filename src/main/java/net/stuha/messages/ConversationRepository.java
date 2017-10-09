package net.stuha.messages;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ConversationRepository extends CrudRepository<Conversation, UUID> {
    @Query(value = "SELECT c.* FROM user_conversation uc LEFT JOIN conversation c ON uc.conversation_id = c.id WHERE uc.user_id = ?1", nativeQuery = true)
    List<Conversation> findConversationsByUserId(UUID userId);

    @Query(value = "SELECT c.* FROM user_conversation uc LEFT JOIN conversation c ON uc.conversation_id = c.id WHERE uc.conversation_id = ?1 AND uc.user_id = ?2", nativeQuery = true)
    Conversation findConversationByIdAndUserId(UUID conversationId, UUID userId);
}
