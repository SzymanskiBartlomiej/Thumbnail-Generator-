//package agh.edu.pl.bytomska_bestia.DirectorySearch;
//
//import agh.edu.pl.bytomska_bestia.Image.Image;
//import agh.edu.pl.bytomska_bestia.Enums.ImageSize;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.stream.Stream;
//
//import agh.edu.pl.bytomska_bestia.Queue.ProcessImageTask;
//import agh.edu.pl.bytomska_bestia.Tasks.Task;
//import io.reactivex.rxjava3.core.Observable;
//import io.reactivex.rxjava3.core.ObservableEmitter;
//
//public class RecursivelySearchDirectory {
//    private final String path;
//    private final ImageSize imageSize;
//    private final ArrayList<Image> ImagesList = new ArrayList<>();
//    public RecursivelySearchDirectory(String path, ImageSize imageSize) {
//        this.path = path;
//        this.imageSize = imageSize;
//    }
//    public String getPath() {
//        return path;
//    }
//    public ImageSize getImageSize() {
//        return imageSize;
//    }
//    public void SearchForImages(){
//        try (Stream<Path> walkStream = Files.walk(Paths.get(getPath()))) {
//            walkStream.filter(p -> p.toFile().isFile()).forEach(f -> {
//                if (f.toString().endsWith(".jpg")) {
//                    String nowaSciezka = f.toString().replace("\\", "/");
//                    ImagesList.add(new Image(nowaSciezka, getImageSize()));
//                }
//            });
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    public ArrayList<Image> getImagesList() {
//        return ImagesList;
//    }
//    public Observable<Image> getImagesAsStream(ArrayList<Image> imagesArray) {
//        return Observable.create(emitter -> queueImages(imagesArray, emitter));
//    }
//    public void queueImages(ArrayList<Image> imagesArray, ObservableEmitter<Image> emitter) {
//        try {
//            for (int i = 0; i < imagesArray.size(); i++) {
//                Image image = imagesArray.get(i);
//                Task task = new ProcessImageTask(i, image);
//                task.execute();
//                emitter.onNext(image);
//            }
//        } finally {
//            emitter.onComplete();
//        }
//    }
//}
