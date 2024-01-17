package agh.edu.pl.ThumbnailGenerator.Controller;

import agh.edu.pl.ThumbnailGenerator.Model.Image;
import agh.edu.pl.ThumbnailGenerator.Model.ImageRequest;
import agh.edu.pl.ThumbnailGenerator.Model.ZipRequest;
import agh.edu.pl.ThumbnailGenerator.Service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("images")
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping()
    public ResponseEntity<String> addImage(@RequestBody ImageRequest imageRequest){
        Optional<String> thumbnailsId = imageService.addImage(imageRequest);
        return thumbnailsId.map(s -> new ResponseEntity<>(s, HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PostMapping("/archive")
    public ResponseEntity<List<String>> procesZip(@RequestBody ZipRequest zipRequest){
        try {
            List<String> thumbnailsList = imageService.procesZip(zipRequest);
            return new ResponseEntity<>(thumbnailsList, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getImageById")
    public ResponseEntity<Image> getImageById(@RequestParam String id){
        Optional<Image> image = imageService.getImageById(id);
        return image.map(s -> new ResponseEntity<>(s, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));

    }
    @GetMapping("/getAllImages")
    public ResponseEntity<List<String>> getAllImages(){
        Optional<List<String>> imageIds = imageService.getAllImages();
        return imageIds.map(s -> new ResponseEntity<>(s, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @GetMapping("/getAllFolders")
    public ResponseEntity<List<String>> getAllFolders(){
        Optional<List<String>> imageIds = imageService.getAllFolders();
        return imageIds.map(s -> new ResponseEntity<>(s, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
