package net.stuha.notifications;

import java.util.List;
import java.util.UUID;

public interface UnreadCountDAO {
    List<UnreadCount> readAllUnreadCounts(UUID userId);
}
