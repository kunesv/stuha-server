package net.stuha.notifications;

import java.util.Map;
import java.util.UUID;

public interface UnreadCountService {
    Map<UUID, Long> allUnreadCounts(UUID userId);
}
