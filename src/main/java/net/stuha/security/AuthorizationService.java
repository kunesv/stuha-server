package net.stuha.security;

import javax.servlet.http.HttpServletRequest;

public interface AuthorizationService {
    User authorize(HttpServletRequest request) throws UnauthorizedUserException;
}
