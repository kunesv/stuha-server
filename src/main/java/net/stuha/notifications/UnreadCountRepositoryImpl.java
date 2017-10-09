package net.stuha.notifications;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;

@Repository
public class UnreadCountRepositoryImpl implements UnreadCountRepositoryCustom {
    private static final String ALL_UNREAD_QUERY = "SELECT " +
            "  lv.conversation_id AS conversation_id, " +
            "  count(id) AS unread_count " +
            "FROM message, " +
            "  (SELECT " +
            "     conversation_id, " +
            "     last_visit_on " +
            "   FROM last_visit " +
            "   WHERE cast(user_id AS VARCHAR) = ?1) lv " +
            "WHERE lv.conversation_id = message.conversation_id " +
            "      AND lv.last_visit_on < message.created_on " +
            "GROUP BY lv.conversation_id";

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<UnreadCount> readAllUnreadCounts(UUID userId) {
        final Query query = em.createNativeQuery(ALL_UNREAD_QUERY, "UnreadCount");
        query.setParameter(1, userId.toString());
        return query.getResultList();
    }
}
