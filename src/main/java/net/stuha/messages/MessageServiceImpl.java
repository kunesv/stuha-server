package net.stuha.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private ImageService imageService;

    @Autowired
    private MessageRepository messageRepository;

    public Message add(Message message) {
        message.setId(UUID.randomUUID().toString());

        return messageRepository.save(message);
    }

    public List<Message> find10() {
        List<Message> messages = (List<Message>) messageRepository.findAll();

        return messages;
    }
}
