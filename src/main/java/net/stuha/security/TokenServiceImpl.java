package net.stuha.security;

import org.springframework.beans.factory.annotation.Autowired;
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
        final StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < 128; i++) {
            randomString.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return randomString.toString();
    }
}
