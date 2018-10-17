package net.stuha.notifications;

import net.stuha.messages.Conversation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class UnreadCountRepositoryCustomImpl implements UnreadCountRepositoryCustom {

    // FIXME: This is not the most optimized select ever. Optimize if registered users count exceeds ten.
    private static final String ALL_UNREAD_QUERY = "select " +
            "  cast(c.id AS VARCHAR) id, c.title, c.no_join, c.last_message_on, c.icon_path, " +
            "  COALESCE(count.cnt, 0) unread_count " +
            "from user_conversation uc " +
            "  left join " +
            "  (select " +
            "     m.conversation_id, " +
            "     count(m.conversation_id) cnt " +
            "   from message m " +
            "     join last_visit lv on lv.conversation_id = m.conversation_id " +
            "   where lv.last_visit_on < m.created_on " +
            "       and cast(lv.user_id AS VARCHAR) = ?1 " +
            "   group by m.conversation_id) count " +
            "    on uc.conversation_id = count.conversation_id " +
            "  join conversation c on uc.conversation_id = c.id " +
            "where cast(uc.user_id AS VARCHAR) = ?1 " +
            "order by c.last_message_on DESC";


    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Conversation> readAllUnreadCounts(UUID userId) {
        final Query query = em.createNativeQuery(ALL_UNREAD_QUERY);
        query.setParameter(1, userId.toString());

        return (List<Conversation>) query.getResultList().stream().map(recordToConversation).collect(Collectors.<Conversation>toList());
    }

    private Function<Object[], Conversation> recordToConversation = record -> {
        final Conversation conversation = new Conversation();
        conversation.setId(UUID.fromString((String) record[0]));
        conversation.setTitle((String) record[1]);
        conversation.setNoJoin((Boolean) record[2]);
        conversation.setLastMessageOn(((Timestamp) record[3]).toLocalDateTime());
        conversation.setIconPath((String) record[4]);
        conversation.setUnreadCount(((BigInteger) record[5]).longValue());
        return conversation;
    };
}
