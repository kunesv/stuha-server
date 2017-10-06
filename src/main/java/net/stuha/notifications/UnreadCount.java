package net.stuha.notifications;


import java.util.UUID;

public class UnreadCount {
    private UUID conversationId;
    private Long unreadCount;

    public UnreadCount(UUID conversationId, Long unreadCount) {
        this.conversationId = conversationId;
        this.unreadCount = unreadCount;
    }

    public UUID getConversationId() {
        return conversationId;
    }

    public void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }

    public Long getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Long unreadCount) {
        this.unreadCount = unreadCount;
    }
}
