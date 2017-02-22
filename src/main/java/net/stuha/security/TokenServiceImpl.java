package net.stuha.security;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.EncoderConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public Token generateToken(UUID userId, Boolean autoRevalidate) {
        final Token token = new Token();
        token.setId(UUID.randomUUID());
        token.setUserId(userId);
        token.setToken(newTokenValue());
        token.setCreatedOn(LocalDateTime.now());
        token.setAutoRevalidate(autoRevalidate);

        return tokenRepository.save(token);
    }

    @Override
    public Token validateToken(String token, UUID userId) throws UnauthorizedUserException {
        Token tokenFound = tokenRepository.findByUserIdAndToken(userId, token);
        if (tokenFound == null) {
            throw new UnauthorizedUserException();
        }

        if (LocalDateTime.now().minusMinutes(15).isAfter(tokenFound.getCreatedOn())) {
            if (!tokenFound.getAutoRevalidate() && LocalDateTime.now().minusMinutes(30).isAfter(tokenFound.getCreatedOn())) {
                throw new UnauthorizedUserException();
            }

            tokenFound.setToken(newTokenValue());
            tokenFound.setCreatedOn(LocalDateTime.now());
            tokenFound = tokenRepository.save(tokenFound);
        }

        return tokenFound;
    }

    private String newTokenValue() {
        return ESAPI.randomizer().getRandomString(128, EncoderConstants.CHAR_ALPHANUMERICS);
    }
}
