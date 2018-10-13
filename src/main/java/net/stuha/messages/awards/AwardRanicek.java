package net.stuha.messages.awards;

import net.stuha.messages.Message;
import net.stuha.messages.MessageRepository;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Ranicek
 * Awarded to first message sent to conversation 00000000-0000-0000-0010-000000000001 in the morning (5.30 - 11.00).
 */
public class AwardRanicek extends Award {

    public AwardRanicek(MessageRepository messageRepository, AwardsProperties awardsProperties) {
        super(messageRepository, awardsProperties);
    }

    @Override
    public boolean checkAwardAvailability(final Message message) {
        final LocalDateTime todayMorningStart = LocalDateTime.now().withHour(5).withMinute(30);
        final LocalDateTime todayMorningEnd = LocalDateTime.now().withHour(11).withMinute(0);

        final boolean isMorning = LocalDateTime.now().isAfter(todayMorningStart) && LocalDateTime.now().isBefore(todayMorningEnd);

        return isMorning
                && message.getConversationId().equals(UUID.fromString(getAwardsProperties().getRanicekConversationId()))
                && 0 == getMessageRepository().countAllByConversationIdAndCreatedOnAfter(message.getConversationId(), todayMorningStart);
    }
}
