package net.stuha.messages;

import net.stuha.messages.awards.Standing;

import java.util.List;
import java.util.UUID;

public interface AwardRepositoryCustom {
    List<Standing> findRanicekOverallStandings(UUID conversationId);
}
