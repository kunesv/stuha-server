package net.stuha.security;


import net.stuha.messages.IconRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Autowired
    private IconRepository iconRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    public String validateUserId(String userId) throws UnauthorizedUserException {
        final User user = userRepository.findOne(userId);
        if (user == null) {
            throw new UnauthorizedUserException();
        }
        return user.getId();
    }

    @Override
    public Token validateUserLogin(LoginForm loginForm) throws LoginFailedException {
        final UserCredentials userCredentials = userCredentialsRepository.findByUsernameAndPassword(loginForm.getUsername(), loginForm.getPassword());
        if (userCredentials == null) {
            throw new LoginFailedException();
        }

        final User user = userRepository.findByUsername(userCredentials.getUsername());
        if (user == null) {
            throw new LoginFailedException();
        }

        return tokenService.generateToken(user.getId());
    }

    @Override
    public User getUserDetail(String userId) {
        final User user = userRepository.findOne(userId);
        user.setIcons(iconRepository.findByUserId(userId));

        return user;
    }
}
