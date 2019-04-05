package net.stuha.security;


import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public interface TokenService {

    Token generateToken(UUID userId, Boolean autoRevalidate);

    void validateToken(String token, UUID userId) throws UnauthorizedUserException;

    Token revalidateToken(HttpServletRequest request, UUID userId) throws UnauthorizedUserException;
}
