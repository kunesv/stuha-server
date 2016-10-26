package net.stuha.messages;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
class ImageServiceImpl implements ImageService {

    private List<Image> imageRepository = new ArrayList<>();

    public Image find(String id) throws FileNotFoundException {
        for (Image image : imageRepository) {
            if (StringUtils.equals(id, image.getId())) {
                return image;
            }
        }

        throw new FileNotFoundException("Image not found");
    }

    public Image add(Image image) {
        image.setId(UUID.randomUUID().toString());

        imageRepository.add(image);

        return image;
    }
}
