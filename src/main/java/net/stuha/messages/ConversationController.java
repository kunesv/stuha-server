package net.stuha.messages;


import net.stuha.notifications.UnreadCountService;
import net.stuha.security.AuthorizationService;
import net.stuha.security.UnauthorizedRequestException;
import net.stuha.security.User;
import net.stuha.security.UserConversation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
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

        // Little trimming
        conversation.setTitle(conversation.getTitle().trim());

        try {
            return conversationService.add(conversation, userId);
        } catch (Exception e) {
            LOGGER.error("Adding Conversation failed.", e);
            throw new ConversationExistsException();
        }
    }

    @RequestMapping(value = "/conversation/member", method = RequestMethod.POST)
    public UserConversation addMember(@RequestParam @NotNull UUID conversationId, @RequestParam @NotNull UUID memberId, HttpServletRequest request) throws UnauthorizedRequestException {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);
        final Conversation conversation = conversationService.findConversation(conversationId, userId);

        if (conversation == null || conversation.getNoJoin()) {
            throw new UnauthorizedRequestException();
        }

        return conversationService.addMember(conversationId, memberId);
    }

    @RequestMapping(value = "/conversations", method = RequestMethod.GET)
    public List<Conversation> userConversations(@RequestAttribute(AuthorizationService.GENUINE_USER_ID) UUID userId) {
        return unreadCountService.allUnreadCounts(userId);
    }

    @RequestMapping(value = "/conversation/{conversationId}/members", method = RequestMethod.GET)
    public List<User> conversationMembers(@PathVariable @NotNull UUID conversationId, HttpServletRequest request) throws UnauthorizedRequestException {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        if (!conversationService.userHasConversation(conversationId, userId)) {
            throw new UnauthorizedRequestException();
        }

        return conversationService.findConversationMembers(conversationId);
    }
}
