package net.stuha.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.stuha.messages.formatted.FormattedMessage;
import net.stuha.notifications.LastVisitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);

    private final PictureRepository pictureRepository;
    private final MessageRepository messageRepository;
    private final LastVisitService lastVisitService;
    private final ConversationRepository conversationRepository;
    private final AwardService awardService;

    @Autowired
    public MessageServiceImpl(PictureRepository pictureRepository, MessageRepository messageRepository, LastVisitService lastVisitService, ConversationRepository conversationRepository, AwardService awardService) {
        Assert.notNull(pictureRepository);
        Assert.notNull(messageRepository);
        Assert.notNull(lastVisitService);
        Assert.notNull(conversationRepository);
        Assert.notNull(awardService);

        this.pictureRepository = pictureRepository;
        this.messageRepository = messageRepository;
        this.lastVisitService = lastVisitService;
        this.conversationRepository = conversationRepository;
        this.awardService = awardService;
    }

    @Transactional
    @Override
    public List<Message> add(final Message message, final List<MessageReplyTo> replyTos, final UUID userId) throws InvalidMessageFormatException {
        message.setId(UUID.randomUUID());

        message.setFormatted(formatMessage(message, replyTos).toString());

        final List<Picture> pictures = new ArrayList<>();

        final Message persistentMessage = messageRepository.save(message);

        for (final Picture picture : message.getImages()) {
            final Picture persistentImage = pictureRepository.findOne(picture.getId());

            if (persistentImage == null || !userId.equals(persistentImage.getUserId())) {
                throw new InvalidMessageFormatException();
            }

            persistentImage.setUserId(null);
            persistentImage.setMessageId(persistentMessage.getId());
            pictureRepository.save(persistentImage);
            pictures.add(persistentImage);
        }

        try {
            persistentMessage.setPictures(new ObjectMapper().writeValueAsString(pictures));
        } catch (JsonProcessingException e) {
            throw new InvalidMessageFormatException();
        }

        messageRepository.save(persistentMessage);
        conversationRepository.updateLastMessageOn(persistentMessage.getCreatedOn(), persistentMessage.getConversationId());

        return loadRecent(message.getConversationId(), userId, message.getLastMessageId());
    }

    @Override
    public List<MessageReplyTo> checkReplyTos(List<MessageReplyTo> messageReplyTos, UUID conversationId) {
        List<MessageReplyTo> results = new ArrayList<>();

        for (MessageReplyTo messageReplyTo : messageReplyTos) {
            final Message message = findOne(UUID.fromString(messageReplyTo.getReplyToId()), conversationId);
            if (message != null) {
                messageReplyTo.setIconPath(message.getIconPath());
                messageReplyTo.setCaptionFromFormatted(message.getFormatted());
                results.add(messageReplyTo);
            }
        }
        return results;
    }

    @Transactional
    @Override
    public Messages loadLast10(UUID conversationId, UUID userId) {
        Messages messages = new Messages();
        LocalDateTime lastVisit = lastVisitService.getLastVisitAndUpdate(userId, conversationId);

        if (lastVisit != null) {
            messages.setUnreadCount(messageRepository.countAllByConversationIdAndCreatedOnAfter(conversationId, lastVisit));
        } else {
            messages.setUnreadCount(messageRepository.countAllByConversationId(conversationId));
        }

        messages.getMessages().addAll(messageRepository.findFirst10ByConversationIdOrderByCreatedOnDesc(conversationId));

        return messages;
    }

    @Transactional
    @Override
    public List<Message> loadRecent(UUID conversationId, UUID userId) {
        final LocalDateTime lastVisit = lastVisitService.getLastVisitAndUpdate(userId, conversationId);

        return loadRecent(conversationId, lastVisit);
    }

    @Transactional
    @Override
    public List<Message> loadRecent(UUID conversationId, UUID userId, UUID messageId) {
        lastVisitService.getLastVisitAndUpdate(userId, conversationId);

        if (messageId != null) {
            final Message startFrom = messageRepository.findOne(messageId);

            return loadRecent(conversationId, startFrom.getCreatedOn());
        } else {
            return messageRepository.findByConversationIdOrderByCreatedOnDesc(conversationId);
        }
    }

    private List<Message> loadRecent(UUID conversationId, LocalDateTime loadAfter) {
        return messageRepository.findByConversationIdAndCreatedOnGreaterThanOrderByCreatedOnDesc(conversationId, loadAfter);
    }

    @Transactional
    @Override
    public List<Message> loadMore(UUID conversationId, UUID userId, UUID messageId) {
        lastVisitService.getLastVisitAndUpdate(userId, conversationId);

        final Message startFrom = messageRepository.findOne(messageId);

        return messageRepository.findFirst10ByConversationIdAndCreatedOnLessThanOrderByCreatedOnDesc(conversationId, startFrom.getCreatedOn());
    }

    @Override
    public Message findOne(UUID messageId, UUID conversationId) {
        List<Message> messages = messageRepository.findFirst1ByConversationIdAndId(conversationId, messageId);
        return (messages.size() > 0) ? messages.get(0) : null;
    }

    @Override
    public Message findOne(UUID messageId) {
        return messageRepository.findOne(messageId);
    }


    private FormattedMessage formatMessage(Message message, List<MessageReplyTo> replyTos) {
        final FormattedMessage formattedMessage = new FormattedMessage(message.getRough(), replyTos);

        formattedMessage.setAwards(awardService.computeAwardsForMessage(message));

        return formattedMessage;
    }


}
