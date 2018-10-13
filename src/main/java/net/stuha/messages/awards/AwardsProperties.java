package net.stuha.messages.awards;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "awards")
public class AwardsProperties {
    private String ranicekConversationId;

    public String getRanicekConversationId() {
        return ranicekConversationId;
    }

    public void setRanicekConversationId(String ranicekConversationId) {
        this.ranicekConversationId = ranicekConversationId;
    }
}

