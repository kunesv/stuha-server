package net.stuha.messages;


import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface IconRepository extends CrudRepository<Icon, UUID> {
    List<Icon> findByUserId(UUID userId);
}
