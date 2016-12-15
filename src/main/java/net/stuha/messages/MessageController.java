package net.stuha.messages;

import net.stuha.security.AuthorizationService;
import net.stuha.security.User;
import net.stuha.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class MessageController {
//    private static final Logger LOGGER = LogManager.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public Message add(@ModelAttribute final Message message, HttpServletRequest request) throws Exception {
        Thread.sleep(someDelay());

        final User user = userService.getUserDetail((String) request.getAttribute(AuthorizationService.GENUINE_USER_ID));

        if (!isIconValid(message.getIconPath(), user)) {
            throw new InvalidMessageFormatException();
        }

        message.setUserName(user.getName());
        message.setUserId(user.getId());
        message.setCreatedOn(LocalDateTime.now());
        // FIXME: Validate
        message.setFormatted(message.getRough());

        return messageService.add(message);
    }

    @RequestMapping(value = "/message", method = RequestMethod.GET)
    public List<Message> all() throws Exception {
        Thread.sleep(someDelay());

        return messageService.find10();
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

    private int someDelay() {
        return (int) (Math.random() * 3000);
    }
}
