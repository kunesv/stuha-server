package net.stuha.notifications;

import java.util.List;
import java.util.UUID;

public interface UnreadCountRepositoryCustom {
    List<UnreadCount> readAllUnreadCounts(UUID userId);
}
