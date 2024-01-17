package agh.edu.pl.ThumbnailGenerator;

import agh.edu.pl.ThumbnailGenerator.Model.Image;
import agh.edu.pl.ThumbnailGenerator.Repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
public class ImageRepositoryTest {
    @Autowired
    private ImageRepository imageRepository;

    @BeforeEach
    void setUp() {
        imageRepository.deleteAll();
    }

    @Test
    public void testFindImageByName() {
        Image image = new Image("testImage1", "png", new byte[]{1, 2, 3});
        imageRepository.save(image);

        Image foundImage = imageRepository.findImageByName("testImage1");

        assertNotNull(foundImage);
        assertEquals("testImage1", foundImage.getName());
    }

    @Test
    public void testFindImageById() {
        Image image = new Image("testImage2", "png", new byte[]{1, 2, 3});
        Image savedImage = imageRepository.save(image);

        Image foundImage = imageRepository.findImageById(savedImage.getId());

        assertNotNull(foundImage);
        assertEquals(savedImage.getId(), foundImage.getId());
    }

    @Test
    public void testFindImagesByData() {
        Image image = new Image("testImage3", "png", new byte[]{1, 2, 3, 5, 10});
        imageRepository.save(image);

        List<Image> foundImages = imageRepository.findImagesByData(new byte[]{1, 2, 3, 5, 10});

        assertNotNull(foundImages);
        assertEquals(1, foundImages.size());
        assertEquals("testImage3", foundImages.get(0).getName());
    }
}
