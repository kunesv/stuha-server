package net.stuha.security;


import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public interface UserService {

    UUID validateUserId(UUID userId) throws UnauthorizedUserException;

    Token validateUserLogin(LoginForm loginForm) throws LoginFailedException, NoSuchAlgorithmException;

    User getUserDetail(UUID userId);

    void changePassword(ChangePasswordForm changePasswordForm) throws UnauthorizedUserException;
}
