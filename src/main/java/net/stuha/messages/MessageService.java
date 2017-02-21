package net.stuha.messages;

import java.util.List;

public interface MessageService {
    Message add(Message message);

    List<Message> find10(String conversationId, Long pageNo);
}
