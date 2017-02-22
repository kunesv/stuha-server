package net.stuha.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Image add(Image image) {
        image.setId(UUID.randomUUID());

        return imageRepository.save(image);
    }

    @Override
    public void addAll(List<Image> images, Message message) {

        for (Image image : images) {
            image.setMessageId(message.getId());
            add(image);
        }

    }

    @Override
    public Image find(UUID id) throws ImageNotFoundException {
        for (Image image : imageRepository.findAll()) {
            if (id.equals(image.getId())) {
                return image;
            }
        }

        throw new ImageNotFoundException(id);
    }

    @Override
    public Image thumbnail(UUID id) throws ImageNotFoundException {
        for (Image image : imageRepository.findAll()) {
            if (id.equals(image.getId())) {
                return image;
            }
        }

        throw new ImageNotFoundException(id);
    }

}
