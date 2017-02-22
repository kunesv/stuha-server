package net.stuha.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Override
    public List<Conversation> userConversations(UUID userId) {
        return conversationRepository.findConversationsByUserId(userId);
    }
}
