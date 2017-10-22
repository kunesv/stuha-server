package net.stuha.notifications;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface SubscriptionConversationRepository extends CrudRepository<SubscriptionConversation, UUID> {
}
