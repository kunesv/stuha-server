package net.stuha.security;


import java.security.NoSuchAlgorithmException;

public interface TokenService {

    Token generateToken(String userId) throws NoSuchAlgorithmException;

    Token validateToken(String token, String userId) throws UnauthorizedUserException;

    String newToken();
}
