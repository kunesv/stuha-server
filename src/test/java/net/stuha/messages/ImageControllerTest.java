package net.stuha.messages;

import net.stuha.AbstractControllerTest;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ImageControllerTest extends AbstractControllerTest {

    private MockMvc mockMvc;

    @Test
    public void find() throws Exception {
        mockMvc.perform(get("/image/aaaabbbb-aaaa-bbbb-aaaabbbbcccc"))
                .andExpect(status().isOk());
    }

}