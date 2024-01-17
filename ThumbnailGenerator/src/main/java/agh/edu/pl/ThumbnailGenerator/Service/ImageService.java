package agh.edu.pl.ThumbnailGenerator.Service;

import agh.edu.pl.ThumbnailGenerator.Model.DirectorySearch.UnzipFiles;
import agh.edu.pl.ThumbnailGenerator.Model.Image;
import agh.edu.pl.ThumbnailGenerator.Model.ImageRequest;
import agh.edu.pl.ThumbnailGenerator.Model.ZipRequest;
import agh.edu.pl.ThumbnailGenerator.Repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Optional<Image> getImageByName(String name){
        try{
            Image image = imageRepository.findImageByName(name);
            if(image == null){
                return Optional.empty();
            }
            return Optional.of(image);
        }catch (Exception e){
            return Optional.empty();
        }
    }
    public Optional<Image> getImageById(String id){
        try{
            Image image = imageRepository.findImageById(id);
            if(image == null){
                return Optional.empty();
            }
            return Optional.of(image);
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public Optional<String> addImage(ImageRequest imageRequest){
        try {
            byte[] data = Base64.getDecoder().decode(imageRequest.getData());
            Image image = new Image(imageRequest.getName(), imageRequest.getExtension(), data, imageRequest.getPath());
            imageRepository.save(image);
            return Optional.of(image.getId());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<String> procesZip(ZipRequest zipRequest) {
//      przykładowa ścieżka:  ./src/main/resources/ExampleImages.zip
        UnzipFiles unzipFiles = new UnzipFiles(zipRequest.getPath());
        List<String> thumbnailsList = new ArrayList<>();
        try {
            List<Image> imageList = unzipFiles.readZip();
            for (Image image: imageList) {
                String imageDataInBase64 = Base64.getEncoder().encodeToString(image.getData());
                ImageRequest imageRequest = new ImageRequest(image.getName(), image.getExtension(), imageDataInBase64, "");
                Optional<String> thumbnailId = addImage(imageRequest);
                if (thumbnailId.isPresent() && !thumbnailId.get().isBlank()) {
                    thumbnailsList.add(thumbnailId.get());
                }
            }
            return thumbnailsList;
        } catch (IOException e) {
            throw new RuntimeException("Error while processing ZIP");
        }
    }

    public Optional<List<String>> getAllImages(){
        try{
            List<String> ids = imageRepository.findAllImageIds();
            if(ids == null){
                return Optional.empty();
            }
            return Optional.of(ids);
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public Optional<List<String>> getAllFolders() {
        try{
            List<String> paths = imageRepository.findDistinctPaths().stream().distinct().toList();
            if(paths == null){
                return Optional.empty();
            }
            return Optional.of(paths);
        }catch (Exception e){
            return Optional.empty();
        }
    }
}
