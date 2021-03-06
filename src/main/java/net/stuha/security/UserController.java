package net.stuha.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/currentUser", method = RequestMethod.GET)
    public User currentUser(HttpServletRequest request) {
        UUID currentUserId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        return userService.getUserDetail(currentUserId);
    }


    @RequestMapping(value = "/conversation/{conversationId}/addMember/{name}", method = RequestMethod.GET)
    public List<User> findMembers(@PathVariable UUID conversationId, @PathVariable String name, HttpServletRequest request) {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        return userService.findRelatedUsersByName(name, userId, conversationId);
    }
}
