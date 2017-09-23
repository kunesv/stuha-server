package net.stuha.security;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Token {
    @Id
    private UUID id;

    private UUID userId;
    private String token;

    private LocalDateTime lastUpdate;
    private Boolean autoRevalidate = false;

    private Boolean revalidated = false;

    public Token() {
    }

    public Token(Token tokenToCopy) {
        userId = tokenToCopy.getUserId();
        autoRevalidate = tokenToCopy.getAutoRevalidate();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Boolean getAutoRevalidate() {
        return autoRevalidate;
    }

    public void setAutoRevalidate(Boolean autoRevalidate) {
        this.autoRevalidate = autoRevalidate;
    }

    public Boolean getRevalidated() {
        return revalidated;
    }

    public void setRevalidated(Boolean revalidated) {
        this.revalidated = revalidated;
    }
}
