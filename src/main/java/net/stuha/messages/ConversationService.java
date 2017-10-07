package net.stuha.messages;

import java.util.List;
import java.util.UUID;

public interface ConversationService {
    List<Conversation> userConversations(UUID userId);

    Boolean userHasConversation(UUID conversationId, UUID userId);

    Conversation add(Conversation conversation, UUID userId);
}
