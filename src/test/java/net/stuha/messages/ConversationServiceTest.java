package net.stuha.messages;

import net.stuha.AbstractControllerTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class ConversationServiceTest extends AbstractControllerTest {

    @Autowired
    private ConversationService conversationService;

    @Test
    public void userConversations() throws Exception {

    }

    @Test
    public void userHasConversation() throws Exception {
        assertTrue(conversationService.userHasConversation(
                UUID.fromString("eeee1111-bbbb-cccc-eeee-ffffffffffff"),
                UUID.fromString("ccccccc1-bbbb-cccc-eeee-ffffffffffff")
        ));
    }

}