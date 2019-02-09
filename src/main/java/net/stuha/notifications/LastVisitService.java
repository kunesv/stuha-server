package net.stuha.notifications;

import net.stuha.messages.Message;

import java.time.LocalDateTime;
import java.util.UUID;

public interface LastVisitService {
    LocalDateTime get(UUID userId, UUID conversationId);

    void update(UUID userId, UUID conversationId, Message message);
}
