package net.stuha.messages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class MessageService {

    private static final Logger LOGGER = LogManager.getLogger(MessageService.class);

    private List<Message> messages = new ArrayList<>();

    MessageService() {
        LOGGER.debug("Constructing Service");
    }

    Message add(Message message) {
        messages.add(message);
        return message;
    }

    List<Message> findAll() {
        return messages;
    }
}
