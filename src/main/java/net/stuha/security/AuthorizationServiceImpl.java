package net.stuha.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Override
    public boolean authorize(HttpServletRequest request, HttpServletResponse response) throws UnauthorizedUserException {
        final UUID userId;
        try {
            String uid = request.getHeader("userId");
            if (StringUtils.isBlank(uid)) {
                uid = request.getParameter("userId");
            }
            userId = UUID.fromString(uid);
        } catch (Exception e) {
            throw new UnauthorizedUserException();
        }

        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            token = request.getParameter("token");
        }
        if (StringUtils.isBlank(token)) {
            throw new UnauthorizedUserException();
        }

        Token validToken = tokenService.validateToken(token, userId);
        request.setAttribute(GENUINE_USER_ID, userId);
        if (!StringUtils.equals(token, validToken.getToken())) {
            response.setHeader("token", validToken.getToken());
        }

        return true;
    }
}
