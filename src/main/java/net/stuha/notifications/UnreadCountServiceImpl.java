package net.stuha.notifications;

import net.stuha.messages.Conversation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UnreadCountServiceImpl implements UnreadCountService {

    @Autowired
    private UnreadCountRepositoryCustom unreadCountRepositoryCustom;

    @Override
    public List<Conversation> allUnreadCounts(UUID userId) {
        return unreadCountRepositoryCustom.readAllUnreadCounts(userId);
    }
}
