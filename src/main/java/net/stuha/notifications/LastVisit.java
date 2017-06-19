package net.stuha.notifications;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class LastVisit {
    @Id
    private UUID id;

    private UUID conversationId;

    private UUID userId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getConversationId() {
        return conversationId;
    }

    public void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
