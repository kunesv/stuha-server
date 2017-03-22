package net.stuha.messages;

import net.stuha.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class MessageServiceTest extends AbstractTest {
    @Autowired
    private MessageService messageService;

    @Test
    public void findOne() throws Exception {
        messageService.findOne(UUID.fromString("13c564ab-8bef-490e-b59c-7400dff6ce6b"), UUID.fromString("ccccccc1-bbbb-cccc-eeee-ffffffffffff"));
    }
}