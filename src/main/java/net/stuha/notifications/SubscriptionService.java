package net.stuha.notifications;

import org.jose4j.lang.JoseException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface SubscriptionService {
    Subscription subscribe(Subscription subscription);

    void sendNotifications(UUID conversationId, UUID userId) throws GeneralSecurityException, InterruptedException, JoseException, ExecutionException, IOException;

    SubscriptionConversation addConversation(SubscriptionConversation subscriptionConversation);
}
