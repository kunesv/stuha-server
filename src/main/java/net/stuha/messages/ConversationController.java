package net.stuha.messages;


import net.stuha.notifications.UnreadCount;
import net.stuha.notifications.UnreadCountService;
import net.stuha.security.AuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RestController
public class ConversationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConversationController.class);

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UnreadCountService unreadCountService;

    @RequestMapping(value = "/conversation", method = RequestMethod.POST)
    public Conversation add(@ModelAttribute final Conversation conversation, HttpServletRequest request) throws Exception {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        try {
            return conversationService.add(conversation, userId);
        } catch (Exception e) {
            LOGGER.error("Adding Conversation failed.", e);
            throw new ConversationExistsException();
        }
    }

    @RequestMapping(value = "/conversations", method = RequestMethod.GET)
    public List<Conversation> userConversations(@RequestAttribute(AuthorizationService.GENUINE_USER_ID) UUID userId) {
        return conversationService.userConversations(userId);
    }

    @RequestMapping(value = "/conversations/status", method = RequestMethod.GET)
    public List<UnreadCount> conversationsStatus(@RequestAttribute(AuthorizationService.GENUINE_USER_ID) UUID userId) {
        return unreadCountService.allUnreadCounts(userId);
    }
}
