package net.stuha.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TokenServiceTest {

    private TokenService tokenService;

    @Test
    public void newToken() throws Exception {
        assertTrue(tokenService.generateToken(UUID.randomUUID(), false) != null);
    }

}