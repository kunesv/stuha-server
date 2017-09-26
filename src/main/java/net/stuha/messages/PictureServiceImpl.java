package net.stuha.messages;

import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
class PictureServiceImpl implements PictureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PictureServiceImpl.class);
    public static final int HEIGHT_CONSTRAINT = 1280;
    public static final int WIDTH_CONSTRAINT = 1280;
    public static final int SIZE_CONSTRAINT = 500_000;
    public static final int THUMBNAIL_WIDTH = 240;
    public static final int THUMBNAIL_HEIGHT = 280;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private ThumbnailRepository thumbnailRepository;

    class BigPictureDimensions {
        private int height;
        private int width;

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }

    @Override
    @Transactional
    public List<Picture> addAll(final List<MultipartFile> images, final UUID conversationId, UUID userId) throws IOException {

        return images.stream()
                .map(multipartFile -> {
                    final UUID id = UUID.randomUUID();

                    try {
                        final File file = new File();
                        file.setId(id);
                        file.setConversationId(conversationId);
                        file.setContentType(multipartFile.getContentType());

                        final BigPictureDimensions bigPictureDimensions = new BigPictureDimensions();
                        final BufferedImage bigPicture = ImageIO.read(multipartFile.getInputStream());

                        // FIXME: This should be little bit more clever ...
                        if (multipartFile.getSize() > SIZE_CONSTRAINT) {
                            final ByteArrayOutputStream bigPictureReduced = new ByteArrayOutputStream();
                            Thumbnails.of(multipartFile.getInputStream())
                                    .size(WIDTH_CONSTRAINT, HEIGHT_CONSTRAINT).toOutputStream(bigPictureReduced);
                            file.setFile(bigPictureReduced.toByteArray());

                            bigPictureDimensions.setHeight(HEIGHT_CONSTRAINT);
                            bigPictureDimensions.setWidth(WIDTH_CONSTRAINT);
                        } else {
                            file.setFile(multipartFile.getBytes());

                            bigPictureDimensions.setHeight(bigPicture.getHeight());
                            bigPictureDimensions.setWidth(bigPicture.getWidth());
                        }
                        fileRepository.save(file);

                        final Thumbnail thumbnail = new Thumbnail();
                        thumbnail.setId(id);
                        thumbnail.setConversationId(conversationId);
                        thumbnail.setContentType(multipartFile.getContentType());
                        final ByteArrayOutputStream smallPicture = new ByteArrayOutputStream();
                        Thumbnails.of(multipartFile.getInputStream())
                                .size(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT).toOutputStream(smallPicture);
                        thumbnail.setThumbnail(smallPicture.toByteArray());
                        thumbnail.setPictureHeight(bigPictureDimensions.getHeight());
                        thumbnail.setPictureWidth(bigPictureDimensions.getWidth());
                        thumbnailRepository.save(thumbnail);

                        final Picture picture = new Picture();
                        picture.setId(id);
                        picture.setUserId(userId);
                        picture.setName(multipartFile.getOriginalFilename());
                        return pictureRepository.save(picture);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to create a picture.");
                    }
                }).collect(Collectors.toList());
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
