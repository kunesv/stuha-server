package net.stuha.notifications;

import net.stuha.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

public class UnreadCountRepositoryCustomTest extends AbstractTest {

    @Autowired
    private UnreadCountRepositoryCustom unreadCountRepositoryCustom;

    @Test
    public void readAllUnreadCounts() throws Exception {
        List<UnreadCount> unreadCounts = unreadCountRepositoryCustom.readAllUnreadCounts(UUID.fromString("00000000-0000-f3e5-c5ed-000000000017"));
        assertNotNull("Some or none unread counts expected.", unreadCounts);
    }

}