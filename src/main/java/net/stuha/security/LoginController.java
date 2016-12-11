package net.stuha.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Login functionality
 */
@RestController
public class LoginController {

    @Autowired
    AuthenticationService authenticationService;

    @RequestMapping(value = "/login")
    public User login(@ModelAttribute final LoginForm loginForm) throws Exception {
        Thread.sleep(2000);

        return authenticationService.authenticate(loginForm);
    }

}
