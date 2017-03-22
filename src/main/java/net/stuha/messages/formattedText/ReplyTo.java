package net.stuha.messages.formattedText;

import net.stuha.messages.MessageReplyTo;

public class ReplyTo implements TextNode {
    private String replyToId;
    private String iconPath;
    private String caption;

    public ReplyTo() {
    }

    public ReplyTo(String replyToId, String iconPath, String caption) {
        this.replyToId = replyToId;
        this.iconPath = iconPath;
        this.caption = caption;
    }

    public ReplyTo(MessageReplyTo messageReplyTo) {
        this(messageReplyTo.getReplyToId(), messageReplyTo.getIconPath(), messageReplyTo.getCaption());
    }

    public String getReplyToId() {
        return replyToId;
    }

    public void setReplyToId(String replyToId) {
        this.replyToId = replyToId;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
