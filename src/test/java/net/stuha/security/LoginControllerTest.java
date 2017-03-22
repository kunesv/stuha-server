package net.stuha.security;

import net.stuha.AbstractTest;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class LoginControllerTest extends AbstractTest {

    @Test
    public void login() throws Exception {
        mockMvc.perform(post("/login?username=karel&password=SoMEh4sH"))
                .andExpect(status().isOk());
    }

}