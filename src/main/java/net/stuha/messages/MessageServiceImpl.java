package net.stuha.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private ImageService imageService;

    private List<Message> messageRepository = new ArrayList<>();

    public Message add(Message message) {
        message.setId(UUID.randomUUID().toString());

        for (Image image : message.getImages()) {
            image.setMessageId(message.getId());
            imageService.add(image);

            image.setImage(null);
            image.setThumbnail(null);
        }

        messageRepository.add(message);
        return message;
    }

    public List<Message> find10() {
        return messageRepository;
    }
}
