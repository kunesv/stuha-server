package net.stuha.messages;

import java.util.UUID;

public class MessageWrapper {
    private Message message;
    private UUID userId;

    public MessageWrapper(Message message, UUID userId) {
        this.message = message;
        this.userId = userId;
    }

    public Message getMessage() {
        return message;
    }

    public UUID getUserId() {
        return userId;
    }
}
