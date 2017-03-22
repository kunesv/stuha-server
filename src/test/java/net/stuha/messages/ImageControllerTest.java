package net.stuha.messages;

import net.stuha.AbstractTest;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ImageControllerTest extends AbstractTest {

    @Test
    public void find() throws Exception {
        mockMvc.perform(get("/image/aaaabbbb-aaaa-bbbb-aaaabbbbcccc")
                .header("userId", "eeee1111-bbbb-cccc-eeee-ffffffffffff")
                .header("token", "I5aDtwfM1OrBtgYlsoM7QvOxhqRWDSNzDqxXNWjlzacgnGxaN6EExbRK4IF13JBZp8qvcJDRjaHDxnrlhOWNpnXddSyFUSyqC2JVDDvbwCmlNM3rHqqM2OThuV03X2EP")
        ).andExpect(status().isOk());
    }

}