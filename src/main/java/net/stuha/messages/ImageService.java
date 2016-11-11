package net.stuha.messages;

import java.util.List;

public interface ImageService {

    Image add(Image image);

    void addAll(List<Image> images, Message message);

    Image find(String id) throws ImageNotFoundException;

    Image thumbnail(String id) throws ImageNotFoundException;
}
