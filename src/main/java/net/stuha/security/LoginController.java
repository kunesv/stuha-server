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

    @Autowired
    AuthenticationService authenticationService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public User login(HttpServletRequest request, @ModelAttribute final LoginForm loginForm) throws Exception {
        Thread.sleep(2000);

        final User user = authenticationService.authenticate(loginForm);
        request.setAttribute("user", user);
        return user;
    }

}
