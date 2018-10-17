package net.stuha.messages;

import net.stuha.messages.awards.AwardType;
import net.stuha.messages.awards.Standing;

import java.util.List;
import java.util.UUID;

public interface AwardService {
    List<AwardType> computeAwardsForMessage(Message message);

    List<MessageAward> saveMessageAwards(Message message, List<AwardType> awardTypes);

    List<Standing> ranicekOverallStandings(UUID conversationId);
}
