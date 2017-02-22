package net.stuha.messages;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message add(Message message);

    List<Message> find10(UUID conversationId, Long pageNo);
}
