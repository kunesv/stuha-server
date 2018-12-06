package net.stuha.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Login functionality
 */
@RestController
public class LoginController {

    public static final String PATH = "/login";

    @Autowired
    private UserService userService;

    @RequestMapping(value = PATH, method = RequestMethod.POST)
    public Token login(HttpServletRequest request, @ModelAttribute final LoginForm loginForm) throws Exception {
        return userService.validateUserLogin(loginForm);
    }

}
