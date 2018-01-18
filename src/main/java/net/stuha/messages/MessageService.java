package net.stuha.messages;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    void add(Message message, UUID userId) throws InvalidMessageFormatException;

    List<MessageReplyTo> checkReplyTos(List<MessageReplyTo> messageReplyTos, UUID conversationId);

    Last10Messages loadLast10(UUID conversationId, UUID userId);

//    List<Message> loadRecent(UUID conversationId, UUID userId);

//    List<Message> loadRecent(UUID conversationId, UUID userId, UUID messageId);

    MoreMessages loadMore(UUID conversationId, UUID userId, UUID messageId);

    Message findOne(UUID messageId, UUID conversationId);

    Message findOne(UUID messageId);
}
