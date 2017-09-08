package net.stuha.security;

import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

public class UserServiceImplTest {
    @Test
    public void validateUserId() throws Exception {
    }

    @Test
    public void validateUserLogin() throws Exception {
    }

    @Test
    public void getUserDetail() throws Exception {
    }

    @Test
    public void changePassword() throws Exception {
        final String newPassword = BCrypt.hashpw("test", BCrypt.gensalt());
        System.out.println(newPassword);
    }

}