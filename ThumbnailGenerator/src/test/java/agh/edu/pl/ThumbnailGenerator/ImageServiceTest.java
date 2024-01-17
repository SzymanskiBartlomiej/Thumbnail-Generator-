package agh.edu.pl.ThumbnailGenerator;

import agh.edu.pl.ThumbnailGenerator.Model.Image;
import agh.edu.pl.ThumbnailGenerator.Model.ImageRequest;
import agh.edu.pl.ThumbnailGenerator.Repository.ImageRepository;
import agh.edu.pl.ThumbnailGenerator.Service.ImageService;
import agh.edu.pl.ThumbnailGenerator.Service.ThumbnailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ImageServiceTest {
    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ThumbnailService thumbnailService;

    private ImageService imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageService = new ImageService(imageRepository);
    }

    @Test
    void getImageByName_ReturnsImageAndHttpStatusOK() {
        String imageName = "testImage";
        Image testImage = new Image(imageName, "jpg", new byte[]{});

        when(imageRepository.findImageByName(imageName)).thenReturn(testImage);

        Optional<Image> result = imageService.getImageByName(imageName);

        assertTrue(result.isPresent());

        Image image = result.get();

        assertEquals(testImage, image);
    }

    @Test
    void getImageByName_ReturnsNotFoundWhenImageNotFound() {
        String imageName = "testImage";

        when(imageRepository.findImageByName(imageName)).thenReturn(null);

        Optional<Image> result = imageService.getImageByName(imageName);

        assertFalse(result.isPresent());
    }

    @Test
    void addImage_ReturnsInternalServerErrorWhenThumbnailServiceFails() {
        ImageRequest imageRequest = new ImageRequest("testImage", "jpg", "+ydxVD+F1vKDJL49P92Fjw==");

        when(thumbnailService.addThumbnail(any())).thenReturn(Optional.empty()); // Zakładam, że w przypadku błędu metoda zwraca null

        Optional<String> result = imageService.addImage(imageRequest);

        assertFalse(result.isPresent());
    }

}
