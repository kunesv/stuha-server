package net.stuha.messages;

import net.stuha.security.UserConversation;
import net.stuha.security.UserConversationRepository;
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
    private UserConversationRepository userConversationRepository;

    @Override
    public List<Conversation> userConversations(UUID userId) {
        return conversationRepository.findConversationsByUserId(userId);
    }

    @Override
    public Boolean userHasConversation(UUID conversationId, UUID userId) {
        return conversationRepository.findConversationByIdAndUserId(conversationId, userId) > 0;
    }

    @Transactional
    @Override
    public Conversation add(final Conversation conversation, UUID userId) {
        conversation.setId(UUID.randomUUID());
        conversationRepository.save(conversation);

        final UserConversation userConversation = new UserConversation();
        userConversation.setId(UUID.randomUUID());
        userConversation.setUserId(userId);
        userConversation.setConversationId(conversation.getId());
        userConversationRepository.save(userConversation);

        return conversation;
    }
}
