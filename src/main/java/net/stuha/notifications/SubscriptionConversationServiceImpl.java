package net.stuha.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SubscriptionConversationServiceImpl implements SubscriptionConversationService {

    @Autowired
    private SubscriptionConversationRepository subscriptionConversationRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;


    @Override
    public SubscriptionConversation add(final SubscriptionConversation subscriptionConversation) {

        subscriptionConversation.setId(UUID.randomUUID());

        return subscriptionConversationRepository.save(subscriptionConversation);
    }


}
