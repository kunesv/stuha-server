package net.stuha.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TokenServiceImplTest {
    @Autowired
    private TokenService tokenService;

    @Test
    public void newToken() throws Exception {
        System.out.println(tokenService.newToken());
    }

}