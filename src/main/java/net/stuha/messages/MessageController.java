package net.stuha.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.stuha.messages.formattedText.FormattedText;
import net.stuha.notifications.SubscriptionService;
import net.stuha.security.AuthorizationService;
import net.stuha.security.UnauthorizedRequestException;
import net.stuha.security.User;
import net.stuha.security.UserService;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private SubscriptionService subscriptionService;

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public void add(@ModelAttribute final Message message, @RequestParam UUID conversationId, @RequestParam String replyTo, HttpServletRequest request) throws UnauthorizedRequestException, InvalidMessageFormatException, IOException, InterruptedException, GeneralSecurityException, JoseException, ExecutionException {
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
        message.setFormatted(new FormattedText(message.getRough(), replyTos).toString());

        messageService.add(message, userId);

        subscriptionService.sendNotifications(message.getConversationId(), userId, message);
    }

    @RequestMapping(value = "/messages/{conversationId}/load", method = RequestMethod.GET)
    public Last10Messages load(@PathVariable UUID conversationId, HttpServletRequest request) throws UnauthorizedRequestException {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        if (!conversationService.userHasConversation(conversationId, userId)) {
            throw new UnauthorizedRequestException();
        }

        return messageService.loadLast10(conversationId, userId);
    }

//    @RequestMapping(value = "/messages/{conversationId}/loadRecent/{messageId}", method = RequestMethod.GET)
//    public List<Message> loadRecent(@PathVariable UUID conversationId, @PathVariable UUID messageId, HttpServletRequest request) throws UnauthorizedRequestException {
//        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);
//
//        if (!conversationService.userHasConversation(conversationId, userId)) {
//            throw new UnauthorizedRequestException();
//        }
//
//        return messageService.loadRecent(conversationId, userId, messageId);
//    }

    @RequestMapping(value = "/messages/{conversationId}/loadMore/{messageId}", method = RequestMethod.GET)
    public MoreMessages loadMore(@PathVariable UUID conversationId, @PathVariable UUID messageId, HttpServletRequest request) throws UnauthorizedRequestException {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        if (!conversationService.userHasConversation(conversationId, userId)) {
            throw new UnauthorizedRequestException();
        }

        return messageService.loadMore(conversationId, userId, messageId);
    }

    @RequestMapping(value = "/message", method = RequestMethod.GET)
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
