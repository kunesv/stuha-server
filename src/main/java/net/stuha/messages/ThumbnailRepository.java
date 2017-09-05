package net.stuha.messages;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ThumbnailRepository extends CrudRepository<Thumbnail, UUID> {
}
