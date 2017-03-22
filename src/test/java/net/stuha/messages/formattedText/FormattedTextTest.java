package net.stuha.messages.formattedText;

import net.stuha.messages.MessageReplyTo;
import net.stuha.messages.MessageService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class FormattedTextTest {

    @Autowired
    private MessageService messageService;

    @Test
    public void test() throws Exception {
        FormattedText formatted = new FormattedText("ahoj\n" +
                "jak se mas", null);

    }

    @Test
    public void textProcessing() {
        String rough = "Ahoj @Test User, my friend.\n\rHave you seen http://seznam.cz?";
        System.out.println("Rought: " + rough);

        List<MessageReplyTo> messageReplyTos = new ArrayList<>();
        MessageReplyTo messageReply = new MessageReplyTo();
        messageReply.setReplyToId("111111-1111-1111-1111-111111111111");
        messageReply.setKey("@Test User");
        messageReplyTos.add(messageReply);

        FormattedText formattedText = new FormattedText(rough, messageReplyTos);

        formattedText.getParagraphs().forEach(System.out::println);
    }


}