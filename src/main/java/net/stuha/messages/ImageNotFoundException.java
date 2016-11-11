package net.stuha.messages;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Image not found in repository
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Image")
public class ImageNotFoundException extends Exception {
    public ImageNotFoundException(String id) {
        super(id);
    }
}
