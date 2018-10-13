package net.stuha.messages.awards;

import net.stuha.messages.Message;
import net.stuha.messages.MessageRepository;

public abstract class Award {
    public abstract boolean checkAwardAvailability(MessageRepository messageRepository, Message message);
}
