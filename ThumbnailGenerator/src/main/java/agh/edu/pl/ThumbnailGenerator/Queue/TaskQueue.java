package agh.edu.pl.ThumbnailGenerator.Queue;


import agh.edu.pl.ThumbnailGenerator.Enums.Status;
import agh.edu.pl.ThumbnailGenerator.Model.Thumbnail;
import agh.edu.pl.ThumbnailGenerator.Repository.ThumbnailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class TaskQueue {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ThumbnailRepository thumbnailRepository;
    private final Logger logger = LoggerFactory.getLogger(TaskQueue.class);
    @Autowired
    public TaskQueue(ThumbnailRepository thumbnailRepository) {
        this.thumbnailRepository = thumbnailRepository;
        loadPendingTasks();
    }
    private void loadPendingTasks(){
        List<Thumbnail> toProcess = thumbnailRepository.findThumbnailsByStatus(Status.IN_PROGRESS.toString());
        for (Thumbnail t : toProcess) {
            executor.execute(new ProcessImageTask(t, thumbnailRepository));
        }
    }
    public void queueTask(Thumbnail thumbnail) {
        executor.execute(new ProcessImageTask(thumbnail, thumbnailRepository));
        logger.info("Queued image for processing: " + thumbnail.getName());
    }

}
