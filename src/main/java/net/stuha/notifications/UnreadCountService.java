package net.stuha.notifications;

import net.stuha.messages.Conversation;

import java.util.List;
import java.util.UUID;

public interface UnreadCountService {
    List<Conversation> allUnreadCounts(UUID userId);
}
