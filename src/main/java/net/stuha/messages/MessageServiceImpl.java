package net.stuha.messages;

import net.stuha.notifications.LastVisit;
import net.stuha.notifications.LastVisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private ImageService imageService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private LastVisitRepository lastVisitRepository;


    @Override
    public Message add(Message message) {
        message.setId(UUID.randomUUID());

        return messageRepository.save(message);
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
    public List<Message> find10(UUID conversationId, UUID userId, Long pageNo) {
        LastVisit lastVisit = lastVisitRepository.findFirstByUserIdAndConversationId(userId, conversationId);
        long unreadCount;

        if (lastVisit != null) {
            unreadCount = messageRepository.countAllByConversationIdAndCreatedOnAfter(conversationId, lastVisit.getLastVisitOn());
        } else {
            lastVisit = new LastVisit();
            lastVisit.setId(UUID.randomUUID());
            lastVisit.setConversationId(conversationId);
            lastVisit.setUserId(userId);

            unreadCount = messageRepository.countAllByConversationId(conversationId);
        }

        if (unreadCount > 0) {
// TODO: Select all unread (limit 10)
        }

        // TODO: eventually add some already read ones

        lastVisit.setLastVisitOn(LocalDateTime.now());
        lastVisitRepository.save(lastVisit);

        return messageRepository.findFirst10ByConversationIdOrderByCreatedOnDesc(conversationId);
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
