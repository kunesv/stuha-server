package net.stuha.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.stuha.notifications.LastVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private LastVisitService lastVisitService;


    @Transactional
    @Override
    public void add(final Message message, UUID userId) throws InvalidMessageFormatException {
        message.setId(UUID.randomUUID());
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

        // TODO: send to WebSocket, note that some have bean read ..
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
    public Last10Messages loadLast10(UUID conversationId, UUID userId) {
        Last10Messages last10Messages = new Last10Messages();
        LocalDateTime lastVisit = lastVisitService.getLastVisitAndUpdate(userId, conversationId);

        long totalCount = messageRepository.countAllByConversationId(conversationId);
        long unreadCount = 10;

        if (lastVisit != null) {
            unreadCount = messageRepository.countAllByConversationIdAndCreatedOnAfter(conversationId, lastVisit);
            last10Messages.setRemainingUnreadCount(unreadCount - 10 > 0 ? unreadCount - 10 : 0);
        } else {
            last10Messages.setRemainingUnreadCount(totalCount - 10 > 0 ? totalCount - 10 : 0);
        }

        last10Messages.setMoreToLoad(totalCount > 10);

        if (totalCount > 0) {
            last10Messages.getMessages().addAll(messageRepository.findFirst10ByConversationIdOrderByCreatedOnDesc(conversationId));

            for (int i = 0; i < unreadCount && i < last10Messages.getMessages().size(); i++) {
                last10Messages.getMessages().get(i).setUnread(true);
            }
        }
        return last10Messages;
    }

//    @Transactional
//    @Override
//    public List<Message> loadRecent(UUID conversationId, UUID userId) {
//        final LocalDateTime lastVisit = lastVisitService.getLastVisitAndUpdate(userId, conversationId);
//
//        return loadRecent(conversationId, lastVisit);
//    }
//
//    @Transactional
//    @Override
//    public List<Message> loadRecent(UUID conversationId, UUID userId, UUID messageId) {
//        lastVisitService.getLastVisitAndUpdate(userId, conversationId);
//
//        if (messageId != null) {
//            final Message startFrom = messageRepository.findOne(messageId);
//
//            return loadRecent(conversationId, startFrom.getCreatedOn());
//        } else {
//            return messageRepository.findByConversationIdOrderByCreatedOnDesc(conversationId);
//        }
//    }
//
//    private List<Message> loadRecent(UUID conversationId, LocalDateTime loadAfter) {
//        return messageRepository.findByConversationIdAndCreatedOnGreaterThanOrderByCreatedOnDesc(conversationId, loadAfter);
//    }

    @Transactional
    @Override
    public MoreMessages loadMore(UUID conversationId, UUID userId, UUID messageId) {
        MoreMessages moreMessages = new MoreMessages();

        lastVisitService.getLastVisitAndUpdate(userId, conversationId);

        final Message startFrom = messageRepository.findOne(messageId);

        long remainingCount = messageRepository.countAllByConversationIdAndCreatedOnLessThanOrderByCreatedOnDesc(conversationId, startFrom.getCreatedOn());

        moreMessages.setMoreToLoad(remainingCount > 10);
        moreMessages.setMessages(messageRepository.findFirst10ByConversationIdAndCreatedOnLessThanOrderByCreatedOnDesc(conversationId, startFrom.getCreatedOn()));

        return moreMessages;
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
}
