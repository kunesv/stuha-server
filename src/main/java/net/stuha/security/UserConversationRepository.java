package net.stuha.security;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface UserConversationRepository extends CrudRepository<UserConversation, UUID> {

    List<UserConversation> findAllByConversationId(UUID conversationId);
}
