package net.stuha.security;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

/**
 * To be implemented elsewhere
 */
@Entity
public class UserCredentials {
    @Id
    private UUID id;

    private String username;

    private String password;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
