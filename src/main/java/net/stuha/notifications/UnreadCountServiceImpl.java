package net.stuha.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UnreadCountServiceImpl implements UnreadCountService {

    @Autowired
    private UnreadCountDAO unreadCountDAO;

    @Override
    public List<UnreadCount> allUnreadCounts(UUID userId) {
        return unreadCountDAO.readAllUnreadCounts(userId);
    }
}
