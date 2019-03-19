package net.stuha.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.stuha.notifications.SubscriptionService;
import net.stuha.security.AuthorizationService;
import net.stuha.security.UnauthorizedRequestException;
import net.stuha.security.User;
import net.stuha.security.UserService;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
public class MessageController {
    public static final String REPLY_TO_FORMAT = "<replyTo://%s-%s-%s/>";

    private final MessageService messageService;
    private final UserService userService;
    private final ConversationService conversationService;
    private final SubscriptionService subscriptionService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService, ConversationService conversationService, SubscriptionService subscriptionService) {
        Assert.notNull(messageService);
        Assert.notNull(userService);
        Assert.notNull(conversationService);
        Assert.notNull(subscriptionService);

        this.messageService = messageService;
        this.userService = userService;
        this.conversationService = conversationService;
        this.subscriptionService = subscriptionService;
    }

    @PostMapping(value = "/message")
    public List<Message> add(@ModelAttribute final Message message, @RequestParam UUID conversationId, @RequestParam String replyTo, HttpServletRequest request) throws UnauthorizedRequestException, InvalidMessageFormatException, IOException, InterruptedException, GeneralSecurityException, JoseException, ExecutionException {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        if (!conversationService.userHasConversation(conversationId, userId)) {
            throw new UnauthorizedRequestException();
        }

        final User user = userService.getUserDetail(userId);

        if (!validIcon(message.getIconPath(), user)) {
            throw new InvalidMessageFormatException();
        }

        message.setUserName(user.getName());
        message.setCreatedOn(LocalDateTime.now());

        List<MessageReplyTo> replyTos = messageService.checkReplyTos(messageReplies(replyTo), conversationId);

        final List<Message> recentMessages = messageService.add(message, replyTos, userId);

        subscriptionService.sendNotifications(message.getConversationId(), userId, message);

        return recentMessages;
    }

    @GetMapping(value = "/messages/{conversationId}/loadInitial")
    public Messages loadInitial(@PathVariable UUID conversationId, HttpServletRequest request) throws UnauthorizedRequestException {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        if (!conversationService.userHasConversation(conversationId, userId)) {
            throw new UnauthorizedRequestException();
        }

        return messageService.loadInitial(conversationId, userId);
    }

    @RequestMapping(value = "/messages/{conversationId}/loadRecent/{messageId}", method = RequestMethod.GET)
    public List<Message> loadRecent(@PathVariable UUID conversationId, @PathVariable UUID messageId, HttpServletRequest request) throws UnauthorizedRequestException {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        if (!conversationService.userHasConversation(conversationId, userId)) {
            throw new UnauthorizedRequestException();
        }

        return messageService.loadRecent(conversationId, userId, messageId);
    }

    @RequestMapping(value = "/messages/{conversationId}/loadMore/{messageId}", method = RequestMethod.GET)
    public List<Message> loadMore(@PathVariable UUID conversationId, @PathVariable UUID messageId, HttpServletRequest request) throws UnauthorizedRequestException {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        if (!conversationService.userHasConversation(conversationId, userId)) {
            throw new UnauthorizedRequestException();
        }

        return messageService.loadMore(conversationId, userId, messageId);
    }

    @RequestMapping(value = "/messages/{conversationId}/loadUnread/{messageId}/{unreadCount}", method = RequestMethod.GET)
    public List<Message> loadUnread(@PathVariable UUID conversationId, @PathVariable UUID messageId, @PathVariable int unreadCount, HttpServletRequest request) throws UnauthorizedRequestException {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        if (!conversationService.userHasConversation(conversationId, userId)) {
            throw new UnauthorizedRequestException();
        }

        return messageService.loadUnread(conversationId, userId, messageId, unreadCount);
    }

    @RequestMapping(value = "/messages/{conversationId}/markRead/{messageId}", method = RequestMethod.GET)
    public void markRead(@PathVariable UUID conversationId, @PathVariable UUID messageId, HttpServletRequest request) throws UnauthorizedRequestException {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        if (!conversationService.userHasConversation(conversationId, userId)) {
            throw new UnauthorizedRequestException();
        }

        messageService.markRead(conversationId, userId, messageId);
    }

    @GetMapping(value = "/message")
    public Message one(@RequestParam UUID messageId, HttpServletRequest request) throws UnauthorizedRequestException {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);
        Message message = messageService.findOne(messageId);

        if (!conversationService.userHasConversation(message.getConversationId(), userId)) {
            throw new UnauthorizedRequestException();
        }

        return message;
    }

    private boolean validIcon(final String iconPath, final User user) {
        boolean iconValid = false;
        for (Icon icon : user.getIcons()) {
            if (icon.getPath().equals(iconPath)) {
                iconValid = true;
                break;
            }
        }
        return iconValid;
    }

    private List<MessageReplyTo> messageReplies(final String replyTo) throws IOException {
        List<MessageReplyTo> replies = new ArrayList<>(1);

        if (StringUtils.isNotBlank(replyTo)) {
            ObjectMapper mapper = new ObjectMapper();
            replies = Arrays.asList(mapper.readValue(replyTo, MessageReplyTo[].class));
        }

        return replies;
    }

}
