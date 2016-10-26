package net.stuha.messages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class MessageController {
    private static final Logger LOGGER = LogManager.getLogger(MessageController.class);

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        Assert.notNull(messageService, "Message service must exist!");
        this.messageService = messageService;
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public Message add(@RequestBody final Message message) throws Exception {
        int delay = (int) (Math.random() * 3000);
        LOGGER.debug("delay set to: " + delay);
        Thread.sleep(delay);

        message.setCreatedOn(LocalDateTime.now());
        message.setFormatted(message.getRough());

        return messageService.add(message);
    }

    @RequestMapping(value = "/message", method = RequestMethod.GET)
    public List<Message> all() {
        return messageService.findAll();
    }
}
