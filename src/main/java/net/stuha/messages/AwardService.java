package net.stuha.messages;

import net.stuha.messages.awards.AwardType;

import java.util.List;

public interface AwardService {
    List<AwardType> computeAwardsForMessage(Message message);

    List<MessageAward> saveMessageAwards(Message message, List<AwardType> awardTypes);
}
