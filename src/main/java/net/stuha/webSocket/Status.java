package net.stuha.webSocket;

public class Status extends WebSocketMessage {
    public Status(String title) {
        type = Type.STATUS;
        this.title = title;
    }

    private String title;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
