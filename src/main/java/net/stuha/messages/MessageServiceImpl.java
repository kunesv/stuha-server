package net.stuha.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private ImageService imageService;

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Message add(Message message) {
        message.setId(UUID.randomUUID());

        return messageRepository.save(message);
    }

    @Override
    public List<Message> find10(UUID conversationId, Long pageNo) {
        List<Message> messages = messageRepository.findByConversationId(conversationId);

        return messages;
    }
}
