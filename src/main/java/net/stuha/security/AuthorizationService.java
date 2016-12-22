package net.stuha.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthorizationService {
    String GENUINE_USER_ID = "genuineUserId";

    boolean authorize(HttpServletRequest request, HttpServletResponse response) throws UnauthorizedUserException;
}
