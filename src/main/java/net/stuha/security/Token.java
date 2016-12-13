package net.stuha.security;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Token {
    @Id
    private String id;

    private String userId;
    private String token;

    private LocalDateTime createdOn;
    private Boolean autoRevalidate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public Boolean getAutoRevalidate() {
        return autoRevalidate;
    }

    public void setAutoRevalidate(Boolean autoRevalidate) {
        this.autoRevalidate = autoRevalidate;
    }
}
