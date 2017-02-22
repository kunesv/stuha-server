package net.stuha.messages;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ImageRepository extends CrudRepository<Image, UUID> {
}
