package net.stuha.security;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.EncoderConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public Token generateToken(String userId) throws NoSuchAlgorithmException {
        final Token token = new Token();

        token.setUserId(userId);
        token.setToken(newToken());
        token.setCreatedOn(LocalDateTime.now());

        return tokenRepository.save(token);
    }

    @Override
    public Token validateToken(String token, String userId) throws UnauthorizedUserException {
        Token tokenFound = tokenRepository.findByUserIdAndToken(userId, token);
        if (tokenFound == null) {
            throw new UnauthorizedUserException();
        }

        if (LocalDateTime.now().minusMinutes(15).isAfter(tokenFound.getCreatedOn())) {
            if (!tokenFound.getAutoRevalidate() && LocalDateTime.now().minusMinutes(30).isAfter(tokenFound.getCreatedOn())) {
                throw new UnauthorizedUserException();
            }

            tokenFound.setToken(newToken());
            tokenFound.setCreatedOn(LocalDateTime.now());
            tokenFound = tokenRepository.save(tokenFound);
        }

        return tokenFound;
    }

    public String newToken() {
        return ESAPI.randomizer().getRandomString(128, EncoderConstants.CHAR_ALPHANUMERICS);
    }
}
