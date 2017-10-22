package net.stuha.notifications;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Subscription {

    @Id
    private UUID id;

    private String endpoint;

    private byte[] key;

    private byte[] auth;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public byte[] getAuth() {
        return auth;
    }

    public void setAuth(byte[] auth) {
        this.auth = auth;
    }


}
