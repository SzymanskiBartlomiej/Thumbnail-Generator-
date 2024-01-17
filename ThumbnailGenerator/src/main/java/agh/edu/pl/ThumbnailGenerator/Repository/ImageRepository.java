package agh.edu.pl.ThumbnailGenerator.Repository;

import agh.edu.pl.ThumbnailGenerator.Model.Image;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends MongoRepository<Image, String> {
    Image findImageByName(String name);
    Image findImageById(String id);
    List<Image> findImagesByData(byte[] data);
    @Query(value = "{}", fields = "{_id: 1}")
    List<String> findAllImageIds();
    @Query(value = "{}", fields = "{ '_id' : 0, 'path' : 1}")
    List<String> findDistinctPaths();
}
