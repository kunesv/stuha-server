package net.stuha.security;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class UserConversation {
    @Id
    private UUID id;

    private UUID userId;

    private UUID conversationId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getConversationId() {
        return conversationId;
    }

    public void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }
}
