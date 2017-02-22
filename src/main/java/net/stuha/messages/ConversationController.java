package net.stuha.messages;


import net.stuha.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @RequestMapping(value = "/userConversations", method = RequestMethod.GET)
    public List<Conversation> userConversations(@RequestAttribute(AuthorizationService.GENUINE_USER_ID) UUID userId) {
        return conversationService.userConversations(userId);
    }
}
