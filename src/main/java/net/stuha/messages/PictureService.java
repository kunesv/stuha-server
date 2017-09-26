package net.stuha.messages;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface PictureService {

    List<Picture> addAll(List<MultipartFile> images, UUID conversationId, UUID userId) throws IOException;

    File find(UUID id) throws ImageNotFoundException;

    Thumbnail thumbnail(UUID id) throws ImageNotFoundException;
}
