package net.stuha.messages;

import net.stuha.AbstractTest;
import net.stuha.notifications.LastVisit;
import net.stuha.notifications.LastVisitRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class MessageServiceTest extends AbstractTest {
    @Autowired
    private MessageService messageService;

    @Autowired
    private LastVisitRepository lastVisitRepository;

    @Test
    public void findOne() throws Exception {
        messageService.findOne(UUID.fromString("13c564ab-8bef-490e-b59c-7400dff6ce6b"), UUID.fromString("ccccccc1-bbbb-cccc-eeee-ffffffffffff"));
    }

    @Test
    public void find10() throws Exception {
        LastVisit lastVisit = lastVisitRepository.findFirstByUserIdAndConversationId(UUID.fromString("eeeeeee1-bbbb-cccc-eeee-ffffffffffff"), UUID.fromString("ccccccc1-bbbb-cccc-eeee-ffffffffffff"));

    }

}