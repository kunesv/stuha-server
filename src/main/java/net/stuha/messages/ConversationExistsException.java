package net.stuha.messages;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Duplicate Conversation Title")
public class ConversationExistsException extends Exception {
}
