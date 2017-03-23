package net.stuha.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.stuha.messages.formattedText.FormattedText;
import net.stuha.messages.formattedText.PlainText;
import net.stuha.messages.formattedText.TextNode;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MessageReplyTo {
    private String replyToId;
    private String key;

    private String iconPath;
    private String caption;

    public String getReplyToId() {
        return replyToId;
    }

    public void setReplyToId(String replyToId) {
        this.replyToId = replyToId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public void setCaptionFromFormatted(String formatted) {
        try {
            FormattedText formattedText = new ObjectMapper().readValue(formatted, FormattedText.class);

            Optional<List<TextNode>> nodes = formattedText.getParagraphs().stream()
                    .reduce((textNodes, textNodes2) -> {
                        textNodes.addAll(textNodes2);
                        return textNodes;
                    });
            if (nodes.isPresent()) {
                Optional<String> plainText = nodes.get().stream()
                        .filter(textNode -> textNode instanceof PlainText)
                        .map((textNode) -> ((PlainText) textNode).getText())
                        .reduce((s, s2) -> String.format("%s .. %s", s, s2));

                if (plainText.isPresent()) {
                    caption = plainText.get().trim();

                    if (caption.length() > 28) {
                        caption = caption.substring(0, 28) + "...";
                    }
                }
            }


        } catch (IOException e) {
            throw new IllegalArgumentException("Wrongly formatted message.", e);
        }
    }
}
