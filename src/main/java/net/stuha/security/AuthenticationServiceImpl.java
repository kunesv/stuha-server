package net.stuha.security;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Override
    public User authenticate(LoginForm loginForm) throws LoginFailedException {
        throw new LoginFailedException();
    }
}
