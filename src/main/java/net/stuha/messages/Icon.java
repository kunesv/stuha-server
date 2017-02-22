package net.stuha.messages;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;


@Entity
public class Icon {
    @Id
    private UUID id;

    private String path;
    private String alt;

    private UUID userId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
