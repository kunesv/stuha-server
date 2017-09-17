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
    private ImageRepository imageRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private LastVisitService lastVisitService;


    @Override
    @Transactional
    public Message add(final Message message, UUID userId) throws InvalidMessageFormatException {
        message.setId(UUID.randomUUID());
        final List<String> imageIds = new ArrayList<>();

        final Message persistentMessage = messageRepository.save(message);

        for (final Image image : message.getImages()) {
            final Image persistentImage = imageRepository.findOne(image.getId());

            if (persistentImage == null || !userId.equals(persistentImage.getUserId())) {
                throw new InvalidMessageFormatException();
            }

            persistentImage.setUserId(null);
            persistentImage.setMessageId(persistentMessage.getId());
            imageRepository.save(persistentImage);
            imageIds.add(image.getId().toString());
        }

        try {
            persistentMessage.setImageIds(new ObjectMapper().writeValueAsString(imageIds));
        } catch (JsonProcessingException e) {
            throw new InvalidMessageFormatException();
        }

        return messageRepository.save(persistentMessage);
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

    @Override
    public List<Message> loadRecent(UUID conversationId, UUID userId, UUID messageId) {
        lastVisitService.getLastVisitAndUpdate(userId, conversationId);

        final Message startFrom = messageRepository.findOne(messageId);

        return messageRepository.findByConversationIdAndCreatedOnGreaterThanOrderByCreatedOnDesc(conversationId, startFrom.getCreatedOn());
    }

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
}
