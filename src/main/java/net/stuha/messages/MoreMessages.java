package net.stuha.messages;

import java.util.ArrayList;
import java.util.List;

public class MoreMessages {
    private List<Message> messages = new ArrayList<>();
    private boolean moreToLoad = false;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public boolean isMoreToLoad() {
        return moreToLoad;
    }

    public void setMoreToLoad(boolean moreToLoad) {
        this.moreToLoad = moreToLoad;
    }
}
