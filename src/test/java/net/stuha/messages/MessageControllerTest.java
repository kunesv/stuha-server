package net.stuha.messages;

import net.stuha.AbstractControllerTest;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MessageControllerTest extends AbstractControllerTest {
    @Test
    public void add() throws Exception {

    }

    @Test
    public void all() throws Exception {
        mockMvc.perform(get("/message?userId=1&token=aaa"))
                .andExpect(status().isOk());
    }

}