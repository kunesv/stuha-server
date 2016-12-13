package net.stuha.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Override
    public boolean authorize(HttpServletRequest request) throws UnauthorizedUserException {
        String userId = request.getParameter("userId");
        String token = request.getParameter("token");

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(token)) {
            throw new UnauthorizedUserException();
        }

        request.setAttribute("user", userService.validateUserId(userId));
        request.setAttribute("token", tokenService.validateToken(token, userId));

        return true;
    }
}
