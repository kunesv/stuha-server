package net.stuha.messages.awards;

import net.stuha.messages.Message;
import net.stuha.messages.MessageRepository;

public abstract class Award {

    private final MessageRepository messageRepository;
    private final AwardsProperties awardsProperties;

    public Award(MessageRepository messageRepository, AwardsProperties awardsProperties) {
        this.messageRepository = messageRepository;
        this.awardsProperties = awardsProperties;
    }

    public abstract boolean checkAwardAvailability(Message message);

    public MessageRepository getMessageRepository() {
        return messageRepository;
    }

    public AwardsProperties getAwardsProperties() {
        return awardsProperties;
    }
}
