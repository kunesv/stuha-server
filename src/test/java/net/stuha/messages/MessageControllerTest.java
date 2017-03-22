package net.stuha.messages;

import net.stuha.AbstractTest;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MessageControllerTest extends AbstractTest {
    @Test
    public void add() throws Exception {
//        Message message =  new Message();

//        mockMvc.perform(post("/message", message, ""));
    }

    @Test
    public void all() throws Exception {
        mockMvc.perform(get("/message?userId=1&token=aaa"))
                .andExpect(status().isOk());
    }

}