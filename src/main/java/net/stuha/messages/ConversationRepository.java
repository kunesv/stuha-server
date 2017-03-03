package net.stuha.messages;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ConversationRepository extends CrudRepository<Conversation, UUID> {
    @Query(value = "select c.id as id, c.title as title from user_conversation uc, conversation c where uc.user_id = ?1 and uc.conversation_id = c.id", nativeQuery = true)
    List<Conversation> findConversationsByUserId(UUID userId);

    @Query(value = "select count(*) from user_conversation uc where uc.conversation_id = ?1 and uc.user_id = ?2", nativeQuery = true)
    Long findConversationByIdAndUserId(UUID conversationId, UUID userId);
}
