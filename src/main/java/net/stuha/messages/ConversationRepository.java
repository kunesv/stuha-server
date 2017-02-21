package net.stuha.messages;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConversationRepository extends CrudRepository<Conversation, String> {
    @Query(value = "select c.id as id, c.title as title from user_conversation uc, conversation c where uc.user_id = ?1 and uc.conversation_id = c.id", nativeQuery = true)
    List<Conversation> findConversationsByUserId(String userId);
}
