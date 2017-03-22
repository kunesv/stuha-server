package net.stuha.messages.formattedText;

public class RoughText implements TextNode {
    private String text;

    public RoughText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
