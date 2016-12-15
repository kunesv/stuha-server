package net.stuha.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/currentUser", method = RequestMethod.GET)
    public User currentUser(HttpServletRequest request) {
        String currentUserId = (String) request.getAttribute(AuthorizationService.GENUINE_USER_ID);

        return userService.getUserDetail(currentUserId);
    }
}
