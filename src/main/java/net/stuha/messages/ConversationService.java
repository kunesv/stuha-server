package net.stuha.messages;

import java.util.List;

public interface ConversationService {
    List<Conversation> userConversations(String userId);
}
