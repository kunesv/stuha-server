package net.stuha.security;

import org.springframework.stereotype.Service;

/**
 * Authentication functionality
 */
@Service
public class AuthenticationService {

    public User authenticate(User userToCheck) {
        return userToCheck;
    }
}
