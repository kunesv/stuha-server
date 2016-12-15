package net.stuha.security;

import javax.servlet.http.HttpServletRequest;

public interface AuthorizationService {
    String GENUINE_USER_ID = "genuineUserId";

    boolean authorize(HttpServletRequest request) throws UnauthorizedUserException;
}
