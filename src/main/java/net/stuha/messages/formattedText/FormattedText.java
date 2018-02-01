package net.stuha.messages.formattedText;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.stuha.messages.MessageReplyTo;

import java.util.List;

public class FormattedText {

    private List<TextNode> textNodes;

    private List<ReplyTo> replyTos;

    public FormattedText() {
    }

    public FormattedText(String rough, List<MessageReplyTo> messageReplyTos) {
        textNodes = FormattedTextParser.parseText(rough);
        replyTos = FormattedTextParser.parseReplyTos(messageReplyTos);
    }

    public List<TextNode> getTextNodes() {
        return textNodes;
    }

    public void setTextNodes(List<TextNode> textNodes) {
        this.textNodes = textNodes;
    }

    public List<ReplyTo> getReplyTos() {
        return replyTos;
    }

    public void setReplyTos(List<ReplyTo> replyTos) {
        this.replyTos = replyTos;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Unable to serialize JSON.", e);
        }
    }
}
