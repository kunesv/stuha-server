package net.stuha.security;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserConversationRepository extends CrudRepository<UserConversation, UUID> {
}
