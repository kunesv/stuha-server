package net.stuha.messages.formatted;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "PLAIN_TEXT", value = PlainText.class),
        @JsonSubTypes.Type(name = "LINK", value = Link.class),
        @JsonSubTypes.Type(name = "REPLY_TO", value = ReplyTo.class),
        @JsonSubTypes.Type(name = "NEW_LINE", value = NewLine.class)
})
public abstract class TextNode {
    enum NodeType {
        PLAIN_TEXT, LINK, REPLY_TO, NEW_LINE, ROUGH
    }

    NodeType nodeType;
}
