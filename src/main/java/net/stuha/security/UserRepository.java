package net.stuha.security;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    User findByUsername(String username);

    @Query(value = "SELECT u.* " +
            "FROM (SELECT user_id " +
            "      FROM user_conversation " +
            "      WHERE conversation_id IN (SELECT conversation_id " +
            "                                FROM user_conversation " +
            "                                WHERE user_id = ?2) " +
            "      GROUP BY user_id " +
            "      ORDER BY user_id) uc LEFT JOIN users u ON uc.user_id = u.id " +
            "WHERE upper(name) LIKE upper(?1 || '%') " +
            "      AND u.id NOT IN (SELECT user_id " +
            "                       FROM user_conversation " +
            "                       WHERE conversation_id = ?3)", nativeQuery = true)
    List<User> findRelated(String name, UUID userId, UUID conversationId);

    @Query(value = "SELECT u.* FROM user_conversation uc JOIN users u ON (uc.user_id = u.id) WHERE conversation_id = ?1 ORDER BY u.name ASC", nativeQuery = true)
    List<User> findUserByConversation(UUID conversationId);
}
