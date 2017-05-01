package net.stuha.messages.formattedText;

public class Link extends TextNode {
    private String url;
    private String label;

    public Link() {
        this.nodeType = NodeType.LINK;
    }

    Link(String url, String label) {
        this();
        this.url = url;
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
