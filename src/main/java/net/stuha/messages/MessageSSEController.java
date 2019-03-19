package net.stuha.messages;

import net.stuha.security.AuthorizationService;
import net.stuha.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class MessageSSEController {

    private class UserEmitter {
        private UUID userId;
        private SseEmitter emitter;


        UserEmitter(UUID userId) {
            this.userId = userId;
            this.emitter = new SseEmitter(180_000L);
        }

        UUID getUserId() {
            return userId;
        }

        SseEmitter getEmitter() {
            return emitter;
        }
    }

    private final ConversationService conversationService;

    private final CopyOnWriteArrayList<UserEmitter> emitters = new CopyOnWriteArrayList<>();

    @Autowired
    public MessageSSEController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @GetMapping("/msg")
    public SseEmitter message(HttpServletRequest request, HttpServletResponse response) {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        response.setHeader("Cache-Control", "no-store");

        final UserEmitter emitter = new UserEmitter(userId);

        emitters.add(emitter);

        emitter.getEmitter().onCompletion(() -> emitters.remove(emitter));
        emitter.getEmitter().onTimeout(() -> emitters.remove(emitter));

        return emitter.getEmitter();
    }

    @EventListener
    public void onMessageAdd(MessageWrapper messageWrapper) {

        List<User> users = conversationService.findConversationMembers(messageWrapper.getMessage().getConversationId());

        users.forEach(user -> {
            List<UserEmitter> deadEmitters = new ArrayList<>();

            emitters.forEach(emitter -> {
                if (emitter.getUserId().equals(user.getId())) {
                    try {
                        emitter.getEmitter().send(messageWrapper.getMessage());
                    } catch (Exception e) {
                        deadEmitters.add(emitter);
                    }
                }
            });

            emitters.removeAll(deadEmitters);
        });


    }

}
