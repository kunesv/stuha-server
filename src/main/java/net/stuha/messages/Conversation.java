package net.stuha.messages;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Conversation {

    @Id
    private UUID id;

    private String title;

    private Boolean noJoin;

    private LocalDateTime lastMessageOn;

    private String iconPath;

    @Transient
    private Long unreadCount;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getNoJoin() {
        return noJoin;
    }

    public void setNoJoin(Boolean noJoin) {
        this.noJoin = noJoin;
    }

    public LocalDateTime getLastMessageOn() {
        return lastMessageOn;
    }

    public void setLastMessageOn(LocalDateTime lastMessageOn) {
        this.lastMessageOn = lastMessageOn;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public Long getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Long unreadCount) {
        this.unreadCount = unreadCount;
    }
}
