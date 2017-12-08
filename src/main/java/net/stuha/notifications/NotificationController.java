package net.stuha.notifications;

import net.stuha.messages.ConversationService;
import net.stuha.security.AuthorizationService;
import net.stuha.security.UnauthorizedRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RestController
public class NotificationController {

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private SubscriptionService subscriptionService;

    @RequestMapping(value = "/notification/subscribe", method = RequestMethod.POST)
    public SubscriptionConversation subscribe(@RequestParam String endpoint, @RequestParam String key, @RequestParam String auth, @RequestParam UUID conversationId, HttpServletRequest request) throws UnauthorizedRequestException {

        final Subscription subscription = new Subscription();
        subscription.setEndpoint(endpoint);
        subscription.setKey(Base64.getDecoder().decode(key));
        subscription.setAuth(Base64.getDecoder().decode(auth));

        final Subscription persistentSubscription = subscriptionService.subscribe(subscription);

        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);
        if (!conversationService.userHasConversation(conversationId, userId)) {
            throw new UnauthorizedRequestException();
        }

        final SubscriptionConversation subscriptionConversation = new SubscriptionConversation();
        subscriptionConversation.setSubscriptionId(persistentSubscription.getId());
        subscriptionConversation.setConversationId(conversationId);
        subscriptionConversation.setUserId(userId);

        return subscriptionService.addConversation(subscriptionConversation);
    }

    @RequestMapping(value = "/subscriptions", method = RequestMethod.GET)
    public List<SubscriptionConversation> all(@RequestParam String endpoint, HttpServletRequest request) {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);
        return subscriptionService.getAll(endpoint, userId);
    }

    @RequestMapping(value = "/notification/unsubscribe", method = RequestMethod.POST)
    public void unsubscribe(@RequestParam UUID conversationId, HttpServletRequest request) throws UnauthorizedRequestException {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);
        if (!conversationService.userHasConversation(conversationId, userId)) {
            throw new UnauthorizedRequestException();
        }

        subscriptionService.removeConversation(conversationId, userId);
    }
}
