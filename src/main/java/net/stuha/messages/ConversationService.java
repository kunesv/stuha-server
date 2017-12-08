package net.stuha.messages;

import net.stuha.security.User;
import net.stuha.security.UserConversation;

import java.util.List;
import java.util.UUID;

public interface ConversationService {
    List<Conversation> userConversations(UUID userId);

    Boolean userHasConversation(UUID conversationId, UUID userId);

    Conversation findConversation(UUID conversationId, UUID userId);

    Conversation add(Conversation conversation, UUID userId);

    UserConversation addMember(UUID conversationId, UUID memberId);

    List<User> findConversationMembers(UUID conversationId);
}
