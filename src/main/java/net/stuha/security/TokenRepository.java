package net.stuha.security;


import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TokenRepository extends CrudRepository<Token, String> {
    List<Token> findByUserIdAndToken(String userId, String token);
}
