package net.stuha.messages.awards;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "awards")
public class AwardsProperties {
    private Ranicek ranicek;

    public Ranicek getRanicek() {
        return ranicek;
    }

    public void setRanicek(Ranicek ranicek) {
        this.ranicek = ranicek;
    }
}

