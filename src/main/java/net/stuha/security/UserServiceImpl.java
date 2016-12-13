package net.stuha.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User validateUserId(String userId) throws UnauthorizedUserException {
        User user = userRepository.findOne(userId);
        if (user == null) {
            throw new UnauthorizedUserException();
        }
        return user;
    }
}
