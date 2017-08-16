package net.stuha.messages.formattedText;

public class Link extends TextNode {
    private String url;
    private String label;
    private boolean shortened;

    public Link() {
        this.nodeType = NodeType.LINK;
    }

    Link(String url, String label, boolean shortened) {
        this();
        this.url = url;
        this.label = label;
        this.shortened = shortened;
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

    public boolean isShortened() {
        return shortened;
    }

    public void setShortened(boolean shortened) {
        this.shortened = shortened;
    }
}
