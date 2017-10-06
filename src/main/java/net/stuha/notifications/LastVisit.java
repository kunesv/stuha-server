package net.stuha.notifications;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@SqlResultSetMapping(name = "UnreadCount", classes = {
        @ConstructorResult(targetClass = UnreadCount.class,
                columns = {@ColumnResult(name = "conversation_id", type = UUID.class), @ColumnResult(name = "unread_count", type = Long.class)})
})
public class LastVisit {
    @Id
    private UUID id;

    private UUID conversationId;

    private UUID userId;

    private LocalDateTime lastVisitOn;

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

    public LocalDateTime getLastVisitOn() {
        return lastVisitOn;
    }

    public void setLastVisitOn(LocalDateTime lastVisitOn) {
        this.lastVisitOn = lastVisitOn;
    }
}
