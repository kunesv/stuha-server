package net.stuha.notifications;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface LastVisitRepository extends CrudRepository<LastVisit, UUID> {
    LastVisit findFirstByUserIdAndConversationId(UUID userId, UUID conversationId);
}
