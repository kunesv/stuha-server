package net.stuha.messages;

public class Last10Messages extends MoreMessages {
    private long remainingUnreadCount;

    public long getRemainingUnreadCount() {
        return remainingUnreadCount;
    }

    public void setRemainingUnreadCount(long remainingUnreadCount) {
        this.remainingUnreadCount = remainingUnreadCount;
    }
}
