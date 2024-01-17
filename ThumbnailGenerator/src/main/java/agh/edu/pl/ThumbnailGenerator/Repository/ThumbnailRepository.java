package agh.edu.pl.ThumbnailGenerator.Repository;

import agh.edu.pl.ThumbnailGenerator.Model.Thumbnail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThumbnailRepository extends MongoRepository<Thumbnail, String> {
    Thumbnail findThumbnailByName(String name);
    Thumbnail findThumbnailById(String id);
    @Query("{ 'status' :  ?0 }")
    List<Thumbnail> findThumbnailsByStatus(String status);
}
