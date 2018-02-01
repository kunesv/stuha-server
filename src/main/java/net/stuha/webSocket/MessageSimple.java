package net.stuha.webSocket;

import net.stuha.messages.Message;

import java.util.UUID;

public class MessageSimple extends WebSocketMessage {
    private UUID conversationId;
    private UUID messageId;

    public MessageSimple(Message message) {
        type = Type.NEW_MESSAGE;

        conversationId = message.getConversationId();
        messageId = message.getId();
    }

    public UUID getConversationId() {
        return conversationId;
    }

    public void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }
}
