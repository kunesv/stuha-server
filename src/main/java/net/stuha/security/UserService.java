package net.stuha.security;


import java.security.NoSuchAlgorithmException;

public interface UserService {

    String validateUserId(String userId) throws UnauthorizedUserException;

    Token validateUserLogin(LoginForm loginForm) throws LoginFailedException, NoSuchAlgorithmException;

    User getUserDetail(String userId);
}
