package net.stuha.messages.formattedText;

public class PlainText implements TextNode {
    private String text;

    public PlainText() {
    }

    public PlainText(String rough) {
        this();
        text = rough;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
