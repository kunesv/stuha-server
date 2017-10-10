package net.stuha.notifications;

import java.util.Map;
import java.util.UUID;

public interface UnreadCountRepositoryCustom {
    Map<UUID, Long> readAllUnreadCounts(UUID userId);
}
