package net.stuha.messages;

import java.util.ArrayList;
import java.util.List;

public class Messages {
    private long unreadCount;
    private List<Message> messages = new ArrayList<>();

    public long getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(long unreadCount) {
        this.unreadCount = unreadCount;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
