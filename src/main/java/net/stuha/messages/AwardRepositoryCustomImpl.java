package net.stuha.messages;

import net.stuha.messages.awards.Standing;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class AwardRepositoryCustomImpl implements AwardRepositoryCustom {

    private static final String RANICEK_STANDINGS_QUERY = "SELECT *\n" +
            "FROM (\n" +
            "       SELECT DISTINCT ON (award1.user_name)\n" +
            "         award1.user_name,\n" +
            "         awards_count,\n" +
            "         icon_path\n" +
            "       FROM\n" +
            "         (SELECT\n" +
            "            user_name,\n" +
            "            count(user_name) awards_count\n" +
            "          FROM message_award\n" +
            "          WHERE award_type = 'RANICEK' AND cast(conversation_id AS VARCHAR) = ?1\n" +
            "          GROUP BY user_name\n" +
            "          ORDER BY 2 DESC) award1\n" +
            "         JOIN message_award award2 ON award1.user_name = award2.user_name\n" +
            "\n" +
            "       ORDER BY award1.user_name, created_on DESC) t\n" +
            "ORDER BY awards_count DESC";

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Standing> findRanicekOverallStandings(UUID conversationId) {
        final Query query = em.createNativeQuery(RANICEK_STANDINGS_QUERY);
        query.setParameter(1, conversationId.toString());

        return (List<Standing>) query.getResultList().stream().map(recordToStanding).collect(Collectors.<Conversation>toList());
    }

    private Function<Object[], Standing> recordToStanding = record -> {
        final Standing standing = new Standing();
        standing.setUserName((String) record[0]);
        standing.setCount((BigInteger) record[1]);
        standing.setIconPath((String) record[2]);
        return standing;
    };
}
