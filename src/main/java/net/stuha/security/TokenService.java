package net.stuha.security;


public interface TokenService {

    Token generateToken(String userId, Boolean autoRevalidate);

    Token validateToken(String token, String userId) throws UnauthorizedUserException;

}
