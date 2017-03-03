package net.stuha.messages;

import net.stuha.security.AuthorizationService;
import net.stuha.security.User;
import net.stuha.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
public class MessageController {
//    private static final Logger LOGGER = LogManager.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public Message add(@ModelAttribute final Message message, HttpServletRequest request) throws Exception {
        final User user = userService.getUserDetail((UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID));

        if (!isIconValid(message.getIconPath(), user)) {
            throw new InvalidMessageFormatException();
        }

        message.setUserName(user.getName());
        message.setCreatedOn(LocalDateTime.now());
        // FIXME: Some input validation, no?
        message.setFormatted(message.getRough());

        return messageService.add(message);
    }

    @RequestMapping(value = "/message", method = RequestMethod.GET)
    public List<Message> all(@RequestParam UUID conversationId, @RequestParam Long pageNo) throws Exception {
        return messageService.find10(conversationId, pageNo);
    }


    private boolean isIconValid(String iconPath, User user) {
        boolean iconValid = false;
        for (Icon icon : user.getIcons()) {
            if (icon.getPath().equals(iconPath)) {
                iconValid = true;
                break;
            }
        }
        return iconValid;
    }
}
