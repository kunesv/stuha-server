package net.stuha.notifications;

import java.util.List;
import java.util.UUID;

public interface UnreadCountService {
    List<UnreadCount> allUnreadCounts(UUID userId);
}
