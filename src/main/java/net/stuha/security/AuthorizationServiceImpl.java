package net.stuha.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public User authorize(HttpServletRequest request) throws UnauthorizedUserException {
        String userId = request.getParameter("userId");
        String token = request.getParameter("token");

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(token)) {
            throw new UnauthorizedUserException();
        }

        return authorize(userId, token);
    }


    public User authorize(String userId, String token) throws UnauthorizedUserException {
        List<Token> tokens = tokenRepository.findByUserIdAndToken(userId, token);
        if (tokens.isEmpty()) {
            throw new UnauthorizedUserException();
        }

        User user = userRepository.findOne(tokens.get(0).getUserId());
        if (user == null) {
            throw new UnauthorizedUserException();
        }

        return user;
    }
}
