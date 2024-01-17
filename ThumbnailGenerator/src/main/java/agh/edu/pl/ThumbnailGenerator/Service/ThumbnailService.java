package agh.edu.pl.ThumbnailGenerator.Service;

import agh.edu.pl.ThumbnailGenerator.Enums.Status;
import agh.edu.pl.ThumbnailGenerator.Model.Image;
import agh.edu.pl.ThumbnailGenerator.Model.Thumbnail;
import agh.edu.pl.ThumbnailGenerator.Model.ThumbnailRequest;
import agh.edu.pl.ThumbnailGenerator.Queue.TaskQueue;
import agh.edu.pl.ThumbnailGenerator.Repository.ImageRepository;
import agh.edu.pl.ThumbnailGenerator.Repository.ThumbnailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ThumbnailService {
    private final ThumbnailRepository thumbnailRepository;
    private final ImageRepository imageRepository;
    private final TaskQueue taskQueue;

    @Autowired
    public ThumbnailService(ThumbnailRepository thumbnailRepository, ImageRepository imageRepository, TaskQueue taskQueue) {
        this.thumbnailRepository = thumbnailRepository;
        this.imageRepository = imageRepository;
        this.taskQueue = taskQueue;
    }

    public Optional<Thumbnail> getThumbnailByName(String name){
        try{
            Thumbnail thumbnail = thumbnailRepository.findThumbnailByName(name);
            if(thumbnail == null){
                return Optional.empty();
            }
            return Optional.of(thumbnail);
        }catch (Exception e){
            return Optional.empty();
        }
    }
    public Optional<Thumbnail> getThumbnailById(String id){
        try{
            Thumbnail thumbnail = thumbnailRepository.findThumbnailById(id);
            if(thumbnail == null){
                return Optional.empty();
            }
            return Optional.of(thumbnail);
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public Optional<Status> getThumbnailStatus(String id){
        try{
            Thumbnail thumbnail = thumbnailRepository.findThumbnailById(id);
            if(thumbnail == null){
                return Optional.empty();
            }
            return Optional.of(thumbnail.getStatus());
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public Optional<String> addThumbnail(ThumbnailRequest thumbnailRequest){
        try {
            Image image = imageRepository.findImageById(thumbnailRequest.getImageId());
            Optional<String> thumbnailIdOptional = image.checkIfThumbnailExistsBySize(thumbnailRequest.getImageSize());
            if (thumbnailIdOptional.isPresent()) {
                return thumbnailIdOptional;
            } else {
                Thumbnail thumbnail = new Thumbnail(image, thumbnailRequest.getImageSize(), Status.IN_PROGRESS);
                thumbnail.setName(image.getName() + "_" + thumbnailRequest.getImageSize().toString().toLowerCase());
                thumbnailRepository.save(thumbnail);
                taskQueue.queueTask(thumbnail);

                image.setThumbnailBySize(thumbnail.getId(), thumbnailRequest.getImageSize());
                imageRepository.save(image);
                return Optional.of(thumbnail.getId());
            }

        } catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }


}
