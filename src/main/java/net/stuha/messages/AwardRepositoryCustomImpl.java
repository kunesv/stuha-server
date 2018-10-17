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

    private static final String RANICEK_STANDINGS_QUERY = "SELECT " +
            "user_name, count(user_name) " +
            "FROM message_award " +
            "WHERE award_type = 'RANICEK' AND cast(conversation_id AS VARCHAR) = ?1 " +
            "GROUP BY user_name " +
            "ORDER BY 2 DESC";

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
        return standing;
    };
}
