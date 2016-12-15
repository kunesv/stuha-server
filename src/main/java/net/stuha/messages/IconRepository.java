package net.stuha.messages;


import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IconRepository extends CrudRepository<Icon, String> {
    List<Icon> findByUserId(String userId);
}
