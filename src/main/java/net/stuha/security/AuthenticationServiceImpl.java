package net.stuha.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Override
    public User authenticate(LoginForm loginForm) throws LoginFailedException {
        final UserCredentials userCredentials = userCredentialsRepository.findByUsernameAndPassword(loginForm.getUsername(), loginForm.getPassword());
        if (userCredentials == null) {
            throw new LoginFailedException();
        }

        return userRepository.findByUsername(userCredentials.getUsername());
    }
}
