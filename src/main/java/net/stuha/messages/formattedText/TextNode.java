package net.stuha.messages.formattedText;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "PLAIN_TEXT", value = PlainText.class),
        @JsonSubTypes.Type(name = "LINK", value = Link.class),
        @JsonSubTypes.Type(name = "REPLY_TO", value = ReplyTo.class)
})
public interface TextNode {
}
