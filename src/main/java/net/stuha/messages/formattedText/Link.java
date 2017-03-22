package net.stuha.messages.formattedText;

public class Link implements TextNode {
    private String url;
    private String label;

    public Link() {
    }

    public Link(String url, String label) {
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
