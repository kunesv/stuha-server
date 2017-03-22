package net.stuha.messages.formattedText;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.stuha.messages.MessageReplyTo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FormattedText {
    private static final String PARAGRAPH_SEPARATOR = "[\\r\\n]+";
    private static final String URL_PATTERN = "https?://([a-z0-9.-]+\\.[a-z]+)(/)?([;,/?:@%&=+$a-z0-9_.!~*()\\[\\]#-]*)";

    private List<List<TextNode>> paragraphs;

    public FormattedText(String rough, List<MessageReplyTo> messageReplyTos) {
        paragraphs = Arrays.stream(rough.split(PARAGRAPH_SEPARATOR))
                .map(parseParagraphs())
                .map(parseLinks())
                .map(parseReplyTos(messageReplyTos))
                .map(parseRemainingText())
                .collect(Collectors.toList());
    }

    public List<List<TextNode>> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<List<TextNode>> paragraphs) {
        this.paragraphs = paragraphs;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Unable to serialize JSON.", e);
        }
    }

    private Function<String, List<TextNode>> parseParagraphs() {
        return paragraph -> Collections.singletonList(new RoughText(paragraph));
    }

    private Function<List<TextNode>, List<TextNode>> parseLinks() {
        return paragraph -> paragraph.stream().map(node -> {
            List<TextNode> nodes = new ArrayList<>();
            if (node instanceof RoughText) {
                paragraph.indexOf(node);

                String text = ((RoughText) node).getText();

                Pattern linkPattern = Pattern.compile(URL_PATTERN);
                Matcher linkMatcher = linkPattern.matcher(text);

                int lastMatchEnd = 0;

                while (linkMatcher.find()) {
                    if (linkMatcher.start() > lastMatchEnd) {
                        nodes.add(new RoughText(text.substring(lastMatchEnd, linkMatcher.start())));
                    }
                    nodes.add(new Link(linkMatcher.group(), linkMatcher.group(1)));

                    lastMatchEnd = linkMatcher.end();
                }

                if (lastMatchEnd != text.length() - 1) {
                    nodes.add(new RoughText(text.substring(lastMatchEnd)));
                }
            } else {
                nodes.add(node);
            }
            return nodes;
        }).reduce((textNodes, textNodes2) -> {
            textNodes.addAll(textNodes2);
            return textNodes;
        }).get();
    }

    private Function<List<TextNode>, List<TextNode>> parseReplyTos(List<MessageReplyTo> messageReplyTos) {
        return paragraph -> {
            for (MessageReplyTo messageReplyTo : messageReplyTos) {
                paragraph = paragraph.stream().map(node -> {
                    List<TextNode> nodes = new ArrayList<>();
                    if (node instanceof RoughText) {
                        String text = ((RoughText) node).getText();

                        int lastIndex = 0;

                        while (text.indexOf(messageReplyTo.getKey(), lastIndex) != -1) {
                            int index = text.indexOf(messageReplyTo.getKey(), lastIndex);

                            if (index > lastIndex) {
                                nodes.add(new RoughText(text.substring(lastIndex, index)));
                            }
                            nodes.add(new ReplyTo(messageReplyTo));

                            lastIndex = index + messageReplyTo.getKey().length();
                        }

                        if (lastIndex != text.length() - 1) {
                            nodes.add(new RoughText(text.substring(lastIndex)));
                        }
                    } else {
                        nodes.add(node);
                    }

                    return nodes;
                }).reduce((textNodes, textNodes2) -> {
                    textNodes.addAll(textNodes2);
                    return textNodes;
                }).get();
            }
            return paragraph;
        };
    }

    private Function<List<TextNode>, List<TextNode>> parseRemainingText() {
        return paragraph -> {
            List<TextNode> nodes = new ArrayList<>();
            for (TextNode node : paragraph) {
                if (node instanceof RoughText) {
                    nodes.add(new PlainText(((RoughText) node).getText()));
                } else {
                    nodes.add(node);
                }
            }
            return nodes;
        };
    }
}
