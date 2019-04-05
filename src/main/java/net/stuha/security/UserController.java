package net.stuha.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;


@RestController
public class UserController {

    private final UserService userService;

    private final TokenService tokenService;

    @Autowired
    public UserController(UserService userService, TokenService tokenService) {
        Assert.notNull(userService);
        Assert.notNull(tokenService);

        this.userService = userService;
        this.tokenService = tokenService;
    }

    @GetMapping(value = "/currentUser")
    public User currentUser(HttpServletRequest request) {
        UUID currentUserId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        return userService.getUserDetail(currentUserId);
    }


    @GetMapping(value = "/conversation/{conversationId}/addMember/{name}")
    public List<User> findMembers(@PathVariable UUID conversationId, @PathVariable String name, HttpServletRequest request) {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        return userService.findRelatedUsersByName(name, userId, conversationId);
    }

    @PostMapping(value = "/changePassword")
    public void changePassword(@ModelAttribute ChangePasswordForm changePasswordForm, HttpServletRequest request) throws UnauthorizedRequestException {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);
        final User user = userService.getUserDetail(userId);
        changePasswordForm.setUsername(user.getUsername());

        userService.changePassword(changePasswordForm);
    }

    @GetMapping(value = "/revalidateToken")
    public void revalidateToken(HttpServletRequest request, HttpServletResponse response) throws UnauthorizedRequestException, UnauthorizedUserException {
        final UUID userId = (UUID) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        Token newToken = tokenService.revalidateToken(request, userId);
        response.setHeader("token", newToken.getToken());
    }
}
