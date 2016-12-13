package net.stuha.security;

import javax.servlet.http.HttpServletRequest;

public interface AuthorizationService {
    boolean authorize(HttpServletRequest request) throws UnauthorizedUserException;
}
