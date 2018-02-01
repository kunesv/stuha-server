package net.stuha.webSocket;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "NEW_MESSAGE", value = MessageSimple.class),
        @JsonSubTypes.Type(name = "STATUS", value = Status.class)
})
public abstract class WebSocketMessage {
    enum Type {
        NEW_MESSAGE, STATUS
    }

    Type type;
}
