package net.stuha.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.stuha.messages.formatted.FormattedMessage;
import net.stuha.notifications.LastVisitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Value("${messages.init.load.size}")
    private int initialLoadSize;

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

        final FormattedMessage formattedMessage = formatMessage(message, replyTos);
        formattedMessage.setAwards(awardService.computeAwardsForMessage(message));

        message.setFormatted(formattedMessage.toString());

        final List<Picture> pictures = new ArrayList<>();

        final Message persistentMessage = messageRepository.save(message);

        awardService.saveMessageAwards(persistentMessage, formattedMessage.getAwards());

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
    public Messages loadInitial(UUID conversationId, UUID userId) {
        final Messages messages = new Messages();
        final LocalDateTime lastVisit = lastVisitService.get(userId, conversationId);
        long unreadCount;
        long totalCount = messageRepository.countAllByConversationId(conversationId);

        messages.setTotalCount(totalCount);

        if (lastVisit != null) {
            unreadCount = messageRepository.countAllByConversationIdAndCreatedOnAfter(conversationId, lastVisit);
        } else {
            unreadCount = totalCount;
        }

        messages.setUnreadCount(unreadCount);

        Pageable fewInitial = new PageRequest(0, initialLoadSize, Sort.Direction.DESC, "createdOn");


        messages.getMessages().addAll(messageRepository.findByConversationId(conversationId, fewInitial));

        return messages;
    }

    @Transactional
    @Override
    public List<Message> loadRecent(UUID conversationId, UUID userId, UUID messageId) {
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
        final Message startFrom = messageRepository.findOne(messageId);

        return messageRepository.findFirst10ByConversationIdAndCreatedOnLessThanOrderByCreatedOnDesc(conversationId, startFrom.getCreatedOn());
    }


    @Override
    public List<Message> loadUnread(UUID conversationId, UUID userId, UUID messageId, int unreadCount) {
        final Message startFrom = messageRepository.findOne(messageId);

        Pageable unread = new PageRequest(0, unreadCount > 100 ? 100 : unreadCount, Sort.Direction.DESC, "createdOn");

        return messageRepository.findByConversationIdAndCreatedOnLessThan(conversationId, startFrom.getCreatedOn(), unread);
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

    @Override
    public void markRead(UUID conversationId, UUID userId, UUID messageId) {
        lastVisitService.update(userId, conversationId, findOne(messageId));
    }


    private FormattedMessage formatMessage(Message message, List<MessageReplyTo> replyTos) {
        return new FormattedMessage(message.getRough(), replyTos);
    }


}
