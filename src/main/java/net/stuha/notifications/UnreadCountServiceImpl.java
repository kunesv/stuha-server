package net.stuha.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class UnreadCountServiceImpl implements UnreadCountService {

    @Autowired
    private UnreadCountRepositoryCustom unreadCountRepositoryCustom;

    @Override
    public Map<UUID, Long> allUnreadCounts(UUID userId) {
        return unreadCountRepositoryCustom.readAllUnreadCounts(userId);
    }
}
