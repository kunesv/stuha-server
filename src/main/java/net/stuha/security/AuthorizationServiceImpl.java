package net.stuha.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Override
    public boolean authorize(HttpServletRequest request, HttpServletResponse response) throws UnauthorizedUserException {
        final String userId = request.getHeader("userId");
        final String token = request.getHeader("token");

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(token)) {
            throw new UnauthorizedUserException();
        }

        Token validToken = tokenService.validateToken(token, userId);
        request.setAttribute(GENUINE_USER_ID, userService.validateUserId(validToken.getUserId()));
        if (!StringUtils.equals(token, validToken.getToken())) {
            response.setHeader("token", validToken.getToken());
        }

        return true;
    }
}
