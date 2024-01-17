package agh.edu.pl.ThumbnailGenerator;

import agh.edu.pl.ThumbnailGenerator.Enums.ImageSize;
import agh.edu.pl.ThumbnailGenerator.Enums.Status;
import agh.edu.pl.ThumbnailGenerator.Model.Image;
import agh.edu.pl.ThumbnailGenerator.Model.ImageRequest;
import agh.edu.pl.ThumbnailGenerator.Model.Thumbnail;
import agh.edu.pl.ThumbnailGenerator.Model.ThumbnailRequest;
import agh.edu.pl.ThumbnailGenerator.Queue.TaskQueue;
import agh.edu.pl.ThumbnailGenerator.Repository.ImageRepository;
import agh.edu.pl.ThumbnailGenerator.Repository.ThumbnailRepository;
import agh.edu.pl.ThumbnailGenerator.Service.ImageService;
import agh.edu.pl.ThumbnailGenerator.Service.ThumbnailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ThumbnailServiceTest {
    @Mock
    private ThumbnailRepository thumbnailRepository;
    private ThumbnailService thumbnailService;
    private ImageService imageService;
    private ImageRepository imageRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        thumbnailService = new ThumbnailService(thumbnailRepository, imageRepository, new TaskQueue(thumbnailRepository));
        imageService = new ImageService(imageRepository);
    }

    @Test
    void testGetThumbnailByName() {
        String thumbnailName = "thumbnailName";
        Thumbnail mockThumbnail = new Thumbnail(thumbnailName, "jpg", new byte[0], ImageSize.SMALL, Status.PROCESSED);
        when(thumbnailRepository.findThumbnailByName(thumbnailName)).thenReturn(mockThumbnail);

        Optional<Thumbnail> result = thumbnailService.getThumbnailByName(thumbnailName);

        assertTrue(result.isPresent());

        Thumbnail thumbnailStatus = result.get();

        assertEquals(mockThumbnail, thumbnailStatus);
    }

    @Test
    void testGetThumbnailByNameNotFound() {
        String thumbnailName = "thumbnailName";
        when(thumbnailRepository.findThumbnailByName(thumbnailName)).thenReturn(null);

        Optional<Thumbnail> result = thumbnailService.getThumbnailByName(thumbnailName);

        assertFalse(result.isPresent());
    }

    @Test
    void testGetThumbnailStatus() {
        Thumbnail mockThumbnail = Mockito.mock(Thumbnail.class);

        when(mockThumbnail.getId()).thenReturn("thumbnailId");
        when(mockThumbnail.getStatus()).thenReturn(Status.PROCESSED);
        when(thumbnailRepository.findThumbnailById("thumbnailId")).thenReturn(mockThumbnail);

        Optional<Status> result = thumbnailService.getThumbnailStatus("thumbnailId");

        assertTrue(result.isPresent());

        Status thumbnailStatus = result.get();

        assertEquals(Status.PROCESSED, thumbnailStatus);
    }

    @Test
    void testGetThumbnailStatusNotFound() {
        Thumbnail mockThumbnail = Mockito.mock(Thumbnail.class);

        when(mockThumbnail.getId()).thenReturn("thumbnailId");
        when(thumbnailRepository.findThumbnailById("thumbnailId")).thenReturn(null);

        Optional<Status> result = thumbnailService.getThumbnailStatus("thumbnailId");

        assertFalse(result.isPresent());
    }

    @Test
    void testAddThumbnail() {
        ImageRequest imageRequest = new ImageRequest("thumbnailName", "jpg", "");
        Optional<String> imageId = imageService.addImage(imageRequest);

        if (imageId.isPresent()){
            Image mockImage = imageRepository.findImageById(imageId.get());

            Thumbnail thumbnail = new Thumbnail(mockImage, ImageSize.SMALL, Status.IN_PROGRESS);
            thumbnail.setName(mockImage.getName() + "_" + thumbnail.getSize().toString().toLowerCase());
            when(thumbnailRepository.save(any())).thenReturn(thumbnail);

            ThumbnailRequest thumbnailRequest = new ThumbnailRequest(mockImage.getId(), thumbnail.getSize());

            Optional<String> result = thumbnailService.addThumbnail(thumbnailRequest);

            assertTrue(result.isPresent());
            Thumbnail newThumbnail = thumbnailRepository.findThumbnailById(result.get());
            assertEquals(thumbnail.getName(), newThumbnail.getName());
            assertEquals(thumbnail.getSize(), newThumbnail.getSize());
            assertEquals(thumbnail.getStatus(), newThumbnail.getStatus());
        }
    }
}
