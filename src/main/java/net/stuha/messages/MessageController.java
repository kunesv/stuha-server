package net.stuha.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.stuha.messages.formattedText.FormattedText;
import net.stuha.security.AuthorizationService;
import net.stuha.security.UnauthorizedUserException;
import net.stuha.security.User;
import net.stuha.security.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
public class MessageController {
    public static final String REPLY_TO_FORMAT = "<replyTo://%s-%s-%s/>";

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConversationService conversationService;

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public List<Message> add(@ModelAttribute final Message message, @RequestParam UUID conversationId, @RequestParam String replyTo, HttpServletRequest request) throws Exception {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);
        final User user = userService.getUserDetail(userId);

        if (!conversationService.userHasConversation(conversationId, userId)) {
            throw new InvalidMessageFormatException();
        }

        if (!validIcon(message.getIconPath(), user)) {
            throw new InvalidMessageFormatException();
        }

        message.setUserName(user.getName());
        message.setCreatedOn(LocalDateTime.now());

        List<MessageReplyTo> replyTos = messageService.checkReplyTos(messageReplies(replyTo), conversationId);
        message.setFormatted(new FormattedText(message.getRough(), replyTos).toString());

        return messageService.add(message, userId);
    }

    @RequestMapping(value = "/messages/{conversationId}/load", method = RequestMethod.GET)
    public Messages load(@PathVariable UUID conversationId, HttpServletRequest request) throws Exception {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        if (!conversationService.userHasConversation(conversationId, userId)) {
            throw new UnauthorizedUserException();
        }

        return messageService.loadLast10(conversationId, userId);
    }

    @RequestMapping(value = "/messages/{conversationId}/loadRecent/{messageId}", method = RequestMethod.GET)
    public List<Message> loadRecent(@PathVariable UUID conversationId, @PathVariable UUID messageId, HttpServletRequest request) throws Exception {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        if (!conversationService.userHasConversation(conversationId, userId)) {
            throw new UnauthorizedUserException();
        }

        return messageService.loadRecent(conversationId, userId, messageId);
    }

    @RequestMapping(value = "/messages/{conversationId}/loadMore/{messageId}", method = RequestMethod.GET)
    public List<Message> loadMore(@PathVariable UUID conversationId, @PathVariable UUID messageId, HttpServletRequest request) throws Exception {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        if (!conversationService.userHasConversation(conversationId, userId)) {
            throw new UnauthorizedUserException();
        }

        return messageService.loadMore(conversationId, userId, messageId);
    }

    @RequestMapping(value = "/message", method = RequestMethod.GET)
    public Message one(@RequestParam UUID messageId, HttpServletRequest request) throws UnauthorizedUserException {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);
        Message message = messageService.findOne(messageId);

        if (!conversationService.userHasConversation(message.getConversationId(), userId)) {
            throw new UnauthorizedUserException();
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
