package net.stuha.security;

import org.springframework.stereotype.Service;

/**
 * Authentication functionality
 */
public interface AuthenticationService {
    User authenticate(final LoginForm loginForm) throws LoginFailedException;
}
