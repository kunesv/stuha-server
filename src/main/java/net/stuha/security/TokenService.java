package net.stuha.security;


import java.util.UUID;

public interface TokenService {

    Token generateToken(UUID userId, Boolean autoRevalidate);

    Token findToken(String token, UUID userId);

    Token validateToken(String token, UUID userId) throws UnauthorizedUserException;

}
