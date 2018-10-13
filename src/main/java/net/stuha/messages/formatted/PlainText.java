package net.stuha.messages.formatted;

public class PlainText extends TextNode {
    private String text;

    PlainText() {
        this.nodeType = NodeType.PLAIN_TEXT;
    }

    PlainText(String text) {
        this();
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
