package net.stuha.messages;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message add(Message message);

    List<MessageReplyTo> checkReplyTos(List<MessageReplyTo> messageReplyTos, UUID conversationId);

    List<Message> find10(UUID conversationId, Long pageNo);

    Message findOne(UUID messageId, UUID conversationId);

    Message findOne(UUID messageId);
}
