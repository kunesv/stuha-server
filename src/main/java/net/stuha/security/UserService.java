package net.stuha.security;


import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

public interface UserService {

    Token validateUserLogin(LoginForm loginForm) throws LoginFailedException, NoSuchAlgorithmException;

    User getUserDetail(UUID userId);

    void changePassword(ChangePasswordForm changePasswordForm) throws UnauthorizedRequestException;

    List<User> findRelatedUsersByName(String name, UUID userId, UUID conversationId);
}
