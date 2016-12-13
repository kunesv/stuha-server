package net.stuha.security;


import org.springframework.data.repository.CrudRepository;

public interface UserCredentialsRepository extends CrudRepository<UserCredentials, String> {
    UserCredentials findByUsernameAndPassword(String username, String password);
}
