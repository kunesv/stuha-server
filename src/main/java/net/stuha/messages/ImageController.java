package net.stuha.messages;

import net.stuha.security.AuthorizationService;
import net.stuha.security.UnauthorizedUserException;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ConversationService conversationService;


    @RequestMapping(value = "/image", method = RequestMethod.POST)
    public void add(HttpServletRequest request) throws IOException {
        InputStream is = request.getInputStream();

    }

    @RequestMapping(value = "/image/{id}", method = RequestMethod.GET)
    public Image find(@PathVariable UUID id) throws ImageNotFoundException, InterruptedException {
        Thread.sleep(1000);

        return imageService.find(id);
    }

    @RequestMapping(value = "/thumbnail/{id}", method = RequestMethod.GET)
    public Image thumbnail(@PathVariable UUID id, javax.servlet.http.HttpServletRequest request) throws ImageNotFoundException, InterruptedException, UnauthorizedUserException {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        Image thumbnail = imageService.thumbnail(id);

        if (!conversationService.userHasConversation(thumbnail.getConversationId(), userId)) {
            throw new UnauthorizedUserException();
        }

        return thumbnail;
    }
}
