package net.stuha.messages;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Image not found in repository
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Image")
class ImageNotFoundException extends Exception {
    ImageNotFoundException() {
    }

    ImageNotFoundException(UUID id) {
        super(id.toString());
    }
}
