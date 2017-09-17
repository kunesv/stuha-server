package net.stuha.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LastVisitServiceImpl implements LastVisitService {


    @Autowired
    private LastVisitRepository lastVisitRepository;

    @Override
    public LocalDateTime getLastVisitAndUpdate(UUID userId, UUID conversationId) {
        LastVisit lastVisit = lastVisitRepository.findFirstByUserIdAndConversationId(userId, conversationId);

        LocalDateTime lastVisitDateTime = null;

        if (lastVisit == null) {
            lastVisit = new LastVisit();
            lastVisit.setId(UUID.randomUUID());
            lastVisit.setConversationId(conversationId);
            lastVisit.setUserId(userId);
        } else {
            lastVisitDateTime = LocalDateTime.from(lastVisit.getLastVisitOn());
        }

        lastVisit.setLastVisitOn(LocalDateTime.now());
        lastVisitRepository.save(lastVisit);

        return lastVisitDateTime;
    }


}
