package net.stuha.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Not signed in")
public class UnauthorizedUserException extends Exception {

}
