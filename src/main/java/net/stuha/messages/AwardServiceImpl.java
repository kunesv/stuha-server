package net.stuha.messages;

import net.stuha.messages.awards.Award;
import net.stuha.messages.awards.AwardType;
import net.stuha.messages.awards.AwardsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AwardServiceImpl implements AwardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AwardServiceImpl.class);

    private final MessageRepository messageRepository;
    private final AwardRepository awardRepository;
    private final AwardsProperties awardsProperties;

    public AwardServiceImpl(MessageRepository messageRepository, AwardRepository awardRepository, AwardsProperties awardsProperties) {
        Assert.notNull(messageRepository);
        Assert.notNull(awardRepository);
        Assert.notNull(awardsProperties);

        this.messageRepository = messageRepository;
        this.awardRepository = awardRepository;
        this.awardsProperties = awardsProperties;
    }


    @Override
    public List<AwardType> computeAwardsForMessage(Message message) {
        final List<AwardType> awards = new ArrayList<>();

        for (AwardType awardType : AwardType.values()) {
            try {
                final Award award = awardType.getAwardClass().getConstructor(MessageRepository.class, AwardsProperties.class).newInstance(messageRepository, awardsProperties);
                if (award.checkAwardAvailability(message)) {
                    awards.add(awardType);

                    final MessageAward messageAward = new MessageAward();
                    messageAward.setAwardType(awardType);
                    messageAward.setMessageId(message.getId());
                    messageAward.setUserName(message.getUserName());
                    messageAward.setCreatedOn(LocalDateTime.now());
                    awardRepository.save(messageAward);
                }
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | IllegalArgumentException e) {
                LOGGER.error(String.format("Reflection for award type '%s' not properly set.", awardType), e);
            }
        }

        return awards;
    }
}
