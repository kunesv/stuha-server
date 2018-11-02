package net.stuha.messages;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    List<Message> add(Message message, List<MessageReplyTo> replyTos, UUID userId) throws InvalidMessageFormatException;

    List<MessageReplyTo> checkReplyTos(List<MessageReplyTo> messageReplyTos, UUID conversationId);

    Messages loadLast10(UUID conversationId, UUID userId);

    List<Message> loadRecent(UUID conversationId, UUID userId);

    List<Message> loadRecent(UUID conversationId, UUID userId, UUID messageId);

    List<Message> loadMore(UUID conversationId, UUID userId, UUID messageId);

    List<Message> loadUnread(UUID conversationId, UUID userId, UUID messageId, int unreadCount);

    Message findOne(UUID messageId, UUID conversationId);

    Message findOne(UUID messageId);
}
