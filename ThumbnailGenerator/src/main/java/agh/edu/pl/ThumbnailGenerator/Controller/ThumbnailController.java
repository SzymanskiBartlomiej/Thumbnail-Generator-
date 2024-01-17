package agh.edu.pl.ThumbnailGenerator.Controller;

import agh.edu.pl.ThumbnailGenerator.Enums.Status;
import agh.edu.pl.ThumbnailGenerator.Model.Thumbnail;
import agh.edu.pl.ThumbnailGenerator.Model.ThumbnailRequest;
import agh.edu.pl.ThumbnailGenerator.Service.ThumbnailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("thumbnails")
public class ThumbnailController {
    private final ThumbnailService thumbnailService;

    @Autowired
    public ThumbnailController(ThumbnailService thumbnailService) {
        this.thumbnailService = thumbnailService;
    }

    @GetMapping("/processed")
    public ResponseEntity<Thumbnail> getProcessedThumbnail(@RequestParam String id){
        Optional<Thumbnail> thumbnailsStatus = thumbnailService.getThumbnailById(id);
        return thumbnailsStatus.map(s -> new ResponseEntity<>(s, HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @GetMapping("/status")
    public ResponseEntity<Status> getStatus(@RequestParam String id){
        Optional<Status> thumbnailsStatus = thumbnailService.getThumbnailStatus(id);
        return thumbnailsStatus.map(s -> new ResponseEntity<>(s, HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    public ResponseEntity<String> addThumbnail(@RequestBody ThumbnailRequest thumbnailRequest){
        Optional<String> thumbnailId = thumbnailService.addThumbnail(thumbnailRequest);
        return thumbnailId.map(s -> new ResponseEntity<>(s, HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
