package net.stuha.security;


import org.springframework.data.repository.CrudRepository;

public interface UserCredentialsRepository extends CrudRepository<UserCredentials, String> {
    UserCredentials findByUsername(String username);
}
