package net.stuha.messages;

import java.util.List;
import java.util.UUID;

public interface ImageService {

    Image add(Image image);

    void addAll(List<Image> images, Message message);

    Image find(UUID id) throws ImageNotFoundException;

    Image thumbnail(UUID id) throws ImageNotFoundException;
}
