package net.stuha.notifications;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface SubscriptionConversationRepository extends CrudRepository<SubscriptionConversation, UUID> {
    @Query(value = "SELECT subscription_conversation.* " +
            "FROM subscription " +
            "  LEFT JOIN subscription_conversation ON subscription.id = subscription_conversation.subscription_id " +
            "WHERE endpoint = ?1" +
            "      AND user_id = ?2", nativeQuery = true)
    List<SubscriptionConversation> getAllForUserAndEndpoint(String endpoint, UUID userId);


    SubscriptionConversation findFirstByConversationIdAndUserId(UUID conversationId, UUID userId);
}
