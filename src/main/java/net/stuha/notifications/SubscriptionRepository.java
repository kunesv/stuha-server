package net.stuha.notifications;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface SubscriptionRepository extends CrudRepository<Subscription, UUID> {

    @Query(value = "SELECT subscription.* FROM subscription_conversation LEFT JOIN subscription ON subscription_conversation.subscription_id = subscription.id WHERE conversation_id = ?1 AND user_id != ?2", nativeQuery = true)
    List<Subscription> findSubscriptions(UUID conversationId, UUID userId);
}
