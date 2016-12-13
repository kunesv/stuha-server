package net.stuha.security;


import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, String> {
    Token findByUserIdAndToken(String userId, String token);
}
