package net.stuha.messages;

import java.io.FileNotFoundException;

public interface ImageService {
    Image add(Image image);
    Image find(String id) throws FileNotFoundException;
}
