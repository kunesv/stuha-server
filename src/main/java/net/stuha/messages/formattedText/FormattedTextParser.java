package net.stuha.messages.formattedText;

import net.stuha.messages.MessageReplyTo;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class FormattedTextParser {
    private static final String PARAGRAPH_SEPARATOR = "[\\r\\n]+";
    private static final String URL_PATTERN = "https?://([a-z0-9.-]+\\.[a-z]+)(/)?([;,/?:@%&=+$a-z0-9_.!~*()\\[\\]#-]*)";


    static List<TextNode> parseText(String rough, List<MessageReplyTo> messageReplyTos) {
        messageReplyTos.sort(Comparator.comparingInt(mrt -> -mrt.getKey().length()));

        List<TextNode> links = Stream.of(new RoughText(rough))
                .map(parseNewLines())
                .reduce(new ArrayList<>(), (a, b) -> {
                    a.addAll(b);
                    return a;
                })
                .stream()
                .map(parseLinks())
                .reduce(new ArrayList<>(), (a, b) -> {
                    a.addAll(b);
                    return a;
                });
        List<TextNode> linksAndReplyTos = Stream.of(links).map(parseReplyTos(messageReplyTos)).reduce(new ArrayList<>(), (a, b) -> {
            a.addAll(b);
            return a;
        });

        return linksAndReplyTos.stream().map(parseRemainingText()).collect(Collectors.toList());
    }

    private static Function<TextNode, List<TextNode>> parseNewLines() {
        return node -> {
            List<TextNode> nodes = new ArrayList<>();
            if (TextNode.NodeType.ROUGH == node.nodeType) {
                String text = ((RoughText) node).getText();
                String[] paragraphs = text.split(PARAGRAPH_SEPARATOR);
                for (int i = 0; i < paragraphs.length; i++) {
                    nodes.add(new RoughText(paragraphs[i]));
                    if (paragraphs.length > i + 1) {
                        nodes.add(new NewLine());
                    }
                }
            }
            return nodes;
        };
    }

    private static Function<TextNode, List<TextNode>> parseLinks() {
        return node -> {
            List<TextNode> nodes = new ArrayList<>();
            if (TextNode.NodeType.ROUGH == node.nodeType) {
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

                if (lastMatchEnd < text.length()) {
                    nodes.add(new RoughText(text.substring(lastMatchEnd)));
                }
            } else {
                nodes.add(node);
            }
            return nodes;
        };
    }

    private static Function<List<TextNode>, List<TextNode>> parseReplyTos(List<MessageReplyTo> messageReplyTos) {
        return inNodes -> {
            for (MessageReplyTo messageReplyTo : messageReplyTos) {
                Optional<List<TextNode>> outNodes = inNodes.stream().map(node -> {
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

                        if (lastIndex < text.length()) {
                            nodes.add(new RoughText(text.substring(lastIndex)));
                        }
                    } else {
                        nodes.add(node);
                    }

                    return nodes;
                }).reduce((textNodes, textNodes2) -> {
                    textNodes.addAll(textNodes2);
                    return textNodes;
                });

                if (outNodes.isPresent()) {
                    inNodes = outNodes.get();
                }
            }
            return inNodes;
        };
    }

    private static Function<TextNode, TextNode> parseRemainingText() {
        return node -> {
            if (TextNode.NodeType.ROUGH == node.nodeType) {
                String text = ((RoughText) node).getText();
                if (StringUtils.isNotBlank(text)) {
                    return new PlainText(text);
                }
            }
            return node;
        };
    }
}
