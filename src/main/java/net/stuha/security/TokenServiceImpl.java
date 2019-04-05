package net.stuha.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import se.grunka.fortuna.Fortuna;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private final Random random = Fortuna.createInstance();

    private final TokenRepository tokenRepository;

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Value("${token.revalidateAfter}")
    private int revalidateAfter;

    @Value("${token.invalidateAfter}")
    private int invalidateAfter;

    @Autowired
    public TokenServiceImpl(TokenRepository tokenRepository) {
        Assert.notNull(tokenRepository);

        this.tokenRepository = tokenRepository;
    }

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
    public void validateToken(String token, UUID userId) throws UnauthorizedUserException {
        final Token tokenFound = tokenRepository.findByUserIdAndToken(userId, token);
        if (tokenFound == null) {
            throw new UnauthorizedUserException();
        }
    }

    @Override
    public Token revalidateToken(HttpServletRequest request, UUID userId) throws UnauthorizedUserException {
        String token = request.getHeader("token");

        final Token tokenFound = tokenRepository.findByUserIdAndToken(userId, token);
        if (tokenFound == null) {
            throw new UnauthorizedUserException();
        }

        final Token newToken = new Token(tokenFound);
        newToken.setId(UUID.randomUUID());
        newToken.setLastUpdate(LocalDateTime.now());
        newToken.setToken(newTokenValue());

        tokenRepository.delete(tokenFound.getId());

        return tokenRepository.save(newToken);
    }

    private String newTokenValue() {
        final StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < 128; i++) {
            randomString.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return randomString.toString();
    }
}
