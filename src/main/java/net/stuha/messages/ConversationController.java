package net.stuha.messages;


import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ConversationController {

    public List<Conversation> userConversations(HttpServletRequest request) {
        return new ArrayList<Conversation>();
    }
}
