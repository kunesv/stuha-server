package net.stuha.security;


import net.stuha.messages.IconRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
    public Token validateUserLogin(LoginForm loginForm) throws LoginFailedException {
        final UserCredentials userCredentials = userCredentialsRepository.findByUsername(loginForm.getUsername());
        if (userCredentials == null || !BCrypt.checkpw(loginForm.getPassword(), userCredentials.getPassword())) {
            throw new LoginFailedException();
        }

        final User user = userRepository.findByUsername(userCredentials.getUsername());
        if (user == null) {
            throw new LoginFailedException();
        }

        return tokenService.generateToken(user.getId(), loginForm.getRemember());
    }

    @Override
    public User getUserDetail(UUID userId) {
        final User user = userRepository.findOne(userId);
        user.setIcons(iconRepository.findByUserId(userId));

        return user;
    }

    @Override
    public void changePassword(ChangePasswordForm changePasswordForm) throws UnauthorizedRequestException {
        final UserCredentials userCredentials = userCredentialsRepository.findByUsername(changePasswordForm.getUsername());
        if (!BCrypt.checkpw(changePasswordForm.getPassword(), userCredentials.getPassword())) {
            throw new UnauthorizedRequestException();
        }

        final String newPassword = BCrypt.hashpw(changePasswordForm.getNewPassword(), BCrypt.gensalt());
        userCredentials.setPassword(newPassword);
        userCredentialsRepository.save(userCredentials);
    }

    @Override
    public List<User> findRelatedUsersByName(String name, UUID userId, UUID conversationId) {
        return userRepository.findRelated(name, userId, conversationId);
    }
}
