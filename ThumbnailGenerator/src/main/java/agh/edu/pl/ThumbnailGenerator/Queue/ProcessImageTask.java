package agh.edu.pl.ThumbnailGenerator.Queue;

import agh.edu.pl.ThumbnailGenerator.Enums.Status;
import agh.edu.pl.ThumbnailGenerator.Model.Thumbnail;
import agh.edu.pl.ThumbnailGenerator.Repository.ThumbnailRepository;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProcessImageTask implements Runnable {

    private final Thumbnail thumbnail;
    private final ThumbnailRepository thumbnailRepository;
    private final Logger logger = LoggerFactory.getLogger(ProcessImageTask.class);


    public ProcessImageTask(Thumbnail thumbnail, ThumbnailRepository thumbnailRepository){
        this.thumbnail = thumbnail;
        this.thumbnailRepository = thumbnailRepository;
    }
    @Override
    public void run() {
        execute();
    }
    private void execute() {
        {
            int thumbnailSize = thumbnail.getSize().getThumbnailSize();
            try (ByteArrayInputStream data = new ByteArrayInputStream(thumbnail.getData())){
                BufferedImage sourceImage = ImageIO.read(data);
                BufferedImage thumbnailData = Scalr.resize(sourceImage, Scalr.Mode.FIT_EXACT, thumbnailSize, thumbnailSize);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(thumbnailData, thumbnail.getExtension(), outputStream);

                thumbnail.setData(outputStream.toByteArray());
                thumbnail.setStatus(Status.PROCESSED);
                logger.info("Processed image: "+thumbnail.getName());

            } catch (IOException ex) {
                logger.info("Failed to process image: "+thumbnail.getName());
                thumbnail.setStatus(Status.FAILED);
            } finally {
                thumbnailRepository.save(thumbnail);
            }
        }
    }
}
