package net.stuha.messages.formattedText;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Award {
    RANICEK;

    @JsonValue
    public String toValue() {
        return name();
    }
}
