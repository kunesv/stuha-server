package net.stuha.messages;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
class ImageServiceImpl implements ImageService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ThumbnailRepository thumbnailRepository;

    @Override
    public List<Image> addAll(final List<MultipartFile> images, final UUID conversationId, UUID userId) throws IOException {
        final List<Image> result = new ArrayList<>();
        for (MultipartFile multipartFile : images) {
            File file = new File();
            file.setId(UUID.randomUUID());
            file.setConversationId(conversationId);
            file.setFile(multipartFile.getBytes());
            file.setContentType(multipartFile.getContentType());
            fileRepository.save(file);

            Thumbnail thumbnail = new Thumbnail();
            thumbnail.setId(file.getId());
            thumbnail.setConversationId(conversationId);

            final ByteArrayOutputStream smallImage = new ByteArrayOutputStream();
            Thumbnails.of(new ByteArrayInputStream(multipartFile.getBytes()))
                    .size(240, 240)
                    .toOutputStream(smallImage);
            thumbnail.setThumbnail(smallImage.toByteArray());
            thumbnail.setContentType(multipartFile.getContentType());
            thumbnailRepository.save(thumbnail);

            Image image = new Image();
            image.setId(file.getId());
            image.setUserId(userId);
            image.setName(multipartFile.getOriginalFilename());
            imageRepository.save(image);
            result.add(image);
        }

        return result;
    }

    @Override
    public File find(final UUID id) throws ImageNotFoundException {
        final File image = fileRepository.findOne(id);

        if (image == null) {
            throw new ImageNotFoundException();
        }

        return image;
    }

    @Override
    public Thumbnail thumbnail(UUID id) throws ImageNotFoundException {
        final Thumbnail thumbnail = thumbnailRepository.findOne(id);

        if (thumbnail == null) {
            throw new ImageNotFoundException();
        }

        return thumbnail;
    }

}
