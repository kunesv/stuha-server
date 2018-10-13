package net.stuha.messages.awards;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AwardType {
    RANICEK(AwardRanicek.class);

    private Class<? extends Award> awardClass;

    AwardType(Class<? extends Award> awardClass) {
        this.awardClass = awardClass;
    }
    
    public Class<? extends Award> getAwardClass() {
        return awardClass;
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}
