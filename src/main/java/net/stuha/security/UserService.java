package net.stuha.security;


public interface UserService {

    User validateUserId(String userId) throws UnauthorizedUserException;

}
