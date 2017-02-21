package net.stuha.messages;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, String> {

    @Query("SELECT m FROM Message m where m.conversationId = :conversationId")
    List<Message> findByConversationId(@Param("conversationId") String conversationId);
}
