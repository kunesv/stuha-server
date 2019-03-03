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

        public BigPictureDimensions(int height, int width) {
            this.height = height;
            this.width = width;
        }

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

                        final BufferedImage bigPicture = ImageIO.read(multipartFile.getInputStream());
                        final BigPictureDimensions bigPictureDimensions = new BigPictureDimensions(bigPicture.getHeight(), bigPicture.getWidth());

                        if (!"image/gif".equalsIgnoreCase(file.getContentType()) && multipartFile.getSize() > SIZE_CONSTRAINT) {
                            if (bigPicture.getHeight() > HEIGHT_CONSTRAINT) {
                                bigPictureDimensions.setHeight(HEIGHT_CONSTRAINT);
                            }
                            if (bigPicture.getWidth() > WIDTH_CONSTRAINT) {
                                bigPictureDimensions.setWidth(WIDTH_CONSTRAINT);
                            }

                            final ByteArrayOutputStream bigPictureReduced = new ByteArrayOutputStream();
                            Thumbnails.of(multipartFile.getInputStream())
                                    .outputFormat("jpg")
                                    .size(bigPictureDimensions.getWidth(), bigPictureDimensions.getHeight())
                                    .toOutputStream(bigPictureReduced);
                            file.setContentType("image/jpeg");
                            file.setFile(bigPictureReduced.toByteArray());
                        } else {
                            file.setFile(multipartFile.getBytes());
                        }
                        fileRepository.save(file);

                        final Thumbnail thumbnail = new Thumbnail();
                        thumbnail.setId(id);
                        thumbnail.setConversationId(conversationId);
                        thumbnail.setContentType("image/jpeg");
                        final ByteArrayOutputStream smallPicture = new ByteArrayOutputStream();
                        Thumbnails.of(multipartFile.getInputStream())
                                .outputFormat("jpg")
                                .size(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT).toOutputStream(smallPicture);
                        thumbnail.setThumbnail(smallPicture.toByteArray());
                        thumbnailRepository.save(thumbnail);

                        final Picture picture = new Picture();
                        picture.setId(id);
                        picture.setUserId(userId);
                        picture.setName(multipartFile.getOriginalFilename());
                        picture.setHeight(bigPictureDimensions.getHeight());
                        picture.setWidth(bigPictureDimensions.getWidth());
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
