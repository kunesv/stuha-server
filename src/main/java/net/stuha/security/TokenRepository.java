package net.stuha.security;


import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TokenRepository extends CrudRepository<Token, UUID> {
    Token findByUserIdAndToken(UUID userId, String token);
}
