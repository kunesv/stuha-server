package net.stuha.notifications;

import net.stuha.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LastVisitServiceImpl implements LastVisitService {


    @Autowired
    private LastVisitRepository lastVisitRepository;

    @Override
    public LocalDateTime get(UUID userId, UUID conversationId) {
        final LastVisit lastVisit = lastVisitRepository.findFirstByUserIdAndConversationId(userId, conversationId);
        return lastVisit != null ? lastVisit.getLastVisitOn() : null;
    }

    @Override
    public void update(UUID userId, UUID conversationId, Message message) {
        LastVisit lastVisit = lastVisitRepository.findFirstByUserIdAndConversationId(userId, conversationId);

        if (lastVisit == null) {
            lastVisit = new LastVisit();
            lastVisit.setId(UUID.randomUUID());
            lastVisit.setConversationId(conversationId);
            lastVisit.setUserId(userId);
        }

        lastVisit.setLastVisitOn(message.getCreatedOn());
        lastVisitRepository.save(lastVisit);
    }

}
