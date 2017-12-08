package net.stuha.messages;

import net.stuha.security.User;
import net.stuha.security.UserConversation;
import net.stuha.security.UserConversationRepository;
import net.stuha.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConversationRepository userConversationRepository;

    @Override
    public List<Conversation> userConversations(UUID userId) {
        return conversationRepository.findConversationsByUserId(userId);
    }

    @Override
    public Boolean userHasConversation(UUID conversationId, UUID userId) {
        return findConversation(conversationId, userId) != null;
    }

    @Override
    public Conversation findConversation(UUID conversationId, UUID userId) {
        return conversationRepository.findConversationByIdAndUserId(conversationId, userId);
    }

    @Transactional
    @Override
    public Conversation add(final Conversation conversation, UUID userId) {
        conversation.setId(UUID.randomUUID());
        conversation.setNoJoin(false);
        conversationRepository.save(conversation);

        addMember(conversation.getId(), userId);

        return conversation;
    }

    @Override
    public UserConversation addMember(UUID conversationId, UUID memberId) {
        final UserConversation userConversation = new UserConversation();
        userConversation.setId(UUID.randomUUID());
        userConversation.setUserId(memberId);
        userConversation.setConversationId(conversationId);
        return userConversationRepository.save(userConversation);
    }

    @Override
    public List<User> findConversationMembers(UUID conversationId) {
        return userRepository.findUserByConversation(conversationId);
    }
}
