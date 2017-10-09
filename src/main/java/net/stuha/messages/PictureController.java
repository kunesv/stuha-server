package net.stuha.messages;

import net.stuha.security.AuthorizationService;
import net.stuha.security.UnauthorizedRequestException;
import net.stuha.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
public class PictureController {

    @Autowired
    private PictureService pictureService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConversationService conversationService;


    @RequestMapping(value = "/image", method = RequestMethod.POST)
    public List<Picture> add(@RequestParam("images") List<MultipartFile> images, @RequestParam UUID conversationId, HttpServletRequest request) throws IOException, InvalidMessageFormatException {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        if (!conversationService.userHasConversation(conversationId, userId)) {
            throw new InvalidMessageFormatException();
        }

        return pictureService.addAll(images, conversationId, userId);
    }

    @RequestMapping(value = "/image/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<InputStreamResource> find(@PathVariable UUID id, HttpServletRequest request) throws ImageNotFoundException, InterruptedException, UnauthorizedRequestException {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        File image = pictureService.find(id);

        if (!conversationService.userHasConversation(image.getConversationId(), userId)) {
            throw new UnauthorizedRequestException();
        }

        return ResponseEntity.ok()
                .header("Cache-Control", CacheControl.noCache().getHeaderValue())
                .contentLength(image.getFile().length)
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .body(new InputStreamResource(new ByteArrayInputStream(image.getFile())));
    }

    @RequestMapping(value = "/thumbnail/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<InputStreamResource> thumbnail(@PathVariable UUID id, HttpServletRequest request) throws ImageNotFoundException, InterruptedException, UnauthorizedRequestException {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        Thumbnail thumbnail = pictureService.thumbnail(id);

        if (!conversationService.userHasConversation(thumbnail.getConversationId(), userId)) {
            throw new UnauthorizedRequestException();
        }

        return ResponseEntity.ok()
                .contentLength(thumbnail.getThumbnail().length)
                .contentType(MediaType.parseMediaType(thumbnail.getContentType()))
                .body(new InputStreamResource(new ByteArrayInputStream(thumbnail.getThumbnail())));
    }
}
