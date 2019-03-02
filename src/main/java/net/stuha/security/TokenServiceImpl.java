package net.stuha.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.grunka.fortuna.Fortuna;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private final Random random = Fortuna.createInstance();

    @Autowired
    private TokenRepository tokenRepository;

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Value("${token.revalidateAfter}")
    private int revalidateAfter;

    @Value("${token.invalidateAfter}")
    private int invalidateAfter;

    @Override
    public Token generateToken(UUID userId, Boolean autoRevalidate) {
        final Token token = new Token();
        token.setId(UUID.randomUUID());
        token.setUserId(userId);
        token.setToken(newTokenValue());
        token.setLastUpdate(LocalDateTime.now());
        token.setAutoRevalidate(autoRevalidate);

        return tokenRepository.save(token);
    }

    @Override
    public Token validateToken(String token, UUID userId) throws UnauthorizedUserException {
        final Token tokenFound = tokenRepository.findByUserIdAndToken(userId, token);
        if (tokenFound == null) {
            throw new UnauthorizedUserException();
        }

        if (LocalDateTime.now().minusMinutes(revalidateAfter).isAfter(tokenFound.getLastUpdate())) {
            if (tokenFound.getRevalidated() || (!tokenFound.getAutoRevalidate() && LocalDateTime.now().minusMinutes(invalidateAfter).isAfter(tokenFound.getLastUpdate()))) {
                throw new UnauthorizedUserException();
            }

            final Token newToken = new Token(tokenFound);
            newToken.setId(UUID.randomUUID());
            newToken.setLastUpdate(LocalDateTime.now());
            newToken.setToken(newTokenValue());

            tokenFound.setRevalidated(true);
            tokenFound.setLastUpdate(LocalDateTime.now());
            tokenRepository.save(tokenFound);

            return tokenRepository.save(newToken);
        }

        return tokenFound;
    }

    private String newTokenValue() {
        final StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < 128; i++) {
            randomString.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return randomString.toString();
    }
}
