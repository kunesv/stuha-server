package net.stuha.messages.formatted;

public class RoughText extends TextNode {
    private String text;

    RoughText() {
        this.nodeType = NodeType.ROUGH;
    }

    RoughText(String text) {
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
