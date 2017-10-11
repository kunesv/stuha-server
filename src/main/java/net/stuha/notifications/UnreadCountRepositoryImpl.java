package net.stuha.notifications;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class UnreadCountRepositoryImpl implements UnreadCountRepositoryCustom {
    private static final String ALL_UNREAD_QUERY = "SELECT " +
            "  lv2.conversation_id, " +
            "  COALESCE(lv1.unread_count, 0) unread_count " +
            "FROM last_visit lv2 LEFT JOIN " +
            "  (SELECT " +
            "     lv.conversation_id, " +
            "     count(lv.conversation_id) unread_count " +
            "   FROM last_visit lv " +
            "     LEFT JOIN message m ON m.conversation_id = lv.conversation_id " +
            "   WHERE cast(lv.user_id AS VARCHAR) = ?1 " +
            "         AND lv.last_visit_on < m.created_on " +
            "   GROUP BY lv.conversation_id) lv1 ON lv1.conversation_id = lv2.conversation_id " +
            "WHERE cast(lv2.user_id AS VARCHAR) = ?1";

    @PersistenceContext
    private EntityManager em;

    @Override
    public Map<UUID, Long> readAllUnreadCounts(UUID userId) {
        final Query query = em.createNativeQuery(ALL_UNREAD_QUERY, "UnreadCount");
        query.setParameter(1, userId.toString());
        return ((List<UnreadCount>) query.getResultList())
                .stream()
                .collect(Collectors.toMap(UnreadCount::getConversationId, UnreadCount::getUnreadCount));
    }
}
