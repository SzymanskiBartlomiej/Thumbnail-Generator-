package agh.edu.pl.ThumbnailGenerator;

import agh.edu.pl.ThumbnailGenerator.Enums.ImageSize;
import agh.edu.pl.ThumbnailGenerator.Enums.Status;
import agh.edu.pl.ThumbnailGenerator.Model.Thumbnail;
import agh.edu.pl.ThumbnailGenerator.Repository.ThumbnailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
public class ThumbnailRepositoryTest {
    @Autowired
    private ThumbnailRepository thumbnailRepository;

    @BeforeEach
    void setUp() {
        thumbnailRepository.deleteAll();
    }

    @Test
    public void testFindThumbnailByName() {
        Thumbnail thumbnail = new Thumbnail("testImage1", "png", new byte[]{1, 2, 3}, ImageSize.SMALL, Status.PROCESSED);
        thumbnailRepository.save(thumbnail);

        Thumbnail foundThumbnail = thumbnailRepository.findThumbnailByName("testImage1");

        assertNotNull(foundThumbnail);
        assertEquals("testImage1", foundThumbnail.getName());
    }

    @Test
    public void testFindThumbnailById() {
        Thumbnail thumbnail = new Thumbnail("testImage1", "png", new byte[]{1, 2, 3}, ImageSize.SMALL, Status.PROCESSED);
        thumbnailRepository.save(thumbnail);

        Thumbnail foundThumbnail = thumbnailRepository.findThumbnailById(thumbnail.getId());

        assertNotNull(foundThumbnail);
        assertEquals(thumbnail.getId(), thumbnail.getId());
    }

    @Test
    public void testFindThumbnailsByStatus() {
        Thumbnail thumbnail = new Thumbnail("testImage1", "png", new byte[]{1, 2, 3, 5, 10}, ImageSize.SMALL, Status.PROCESSED);
        thumbnailRepository.save(thumbnail);

        List<Thumbnail> foundThumbnail = thumbnailRepository.findThumbnailsByStatus(Status.PROCESSED.toString());

        assertNotNull(foundThumbnail);
        assertEquals(1, foundThumbnail.size());
        assertEquals("testImage1", foundThumbnail.get(0).getName());
    }
}
