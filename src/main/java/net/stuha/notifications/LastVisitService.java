package net.stuha.notifications;

import java.time.LocalDateTime;
import java.util.UUID;

public interface LastVisitService {
    LocalDateTime getLastVisitAndUpdate(UUID userId, UUID conversationId);
}
