package agh.edu.pl.util;

import agh.edu.pl.enums.ImageSize;
import agh.edu.pl.enums.Status;
import agh.edu.pl.model.Picture;
import agh.edu.pl.model.Thumbnail;
import com.google.gson.*;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;


import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Timer;

public class ThumbnailDownloader {
    private static final int POLL_PERIOD = 100;
    private static final String IMAGE_API_ADDRESS = "http://localhost:8080/images";
    private static final String THUMBNAIL_API_ADDRESS = "http://localhost:8080/thumbnails";

    public  void pollForThumbnail(Thumbnail thumbnail) {
        try {
            thumbnail.setId(postThumbnail(thumbnail));
            ThumbnailPollTask task = new ThumbnailPollTask(thumbnail);
            Timer pollTimer = new Timer();
            pollTimer.schedule(task, 0, POLL_PERIOD);
        } catch (IOException e) {
            thumbnail.changeStatus(Status.FAILED);
        }
    }

    public Observable<Picture> getAllPictures() throws URISyntaxException, IOException {
        Gson gson = new Gson();
        URI uri = new URI(IMAGE_API_ADDRESS + "/getAllImages");
        String responseString = getHTTP(uri);

        responseString = responseString.replaceAll("\\\\", "").replaceAll("\"","");
        JsonArray data = (JsonArray) JsonParser.parseString(responseString);

        List<String> pictureIds = new ArrayList<>();
        for (var el : data.asList()){
            pictureIds.add(el.getAsJsonObject().get("_id").getAsJsonObject().get("$oid").getAsString());
        }
        return Observable.fromIterable(pictureIds).map((String id) -> {
            String pictureResponse = getPicture(id);
            ImageRequestJSON responseJSON = gson.fromJson(pictureResponse, ImageRequestJSON.class);

            return new Picture(responseJSON.getId(), Base64.getDecoder().decode(responseJSON.getData()), responseJSON.getName(), responseJSON.getExtension(), responseJSON.getPath());
        }).subscribeOn(Schedulers.io());
    }

    private String getPicture(String id) throws IOException, URISyntaxException {
        URI uri = new URIBuilder(IMAGE_API_ADDRESS + "/getImageById").addParameter("id", id).build();
        return getHTTP(uri);
    }

    public void postImage(Picture picture) throws IOException {
        Gson gson = new Gson();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost req = new HttpPost(IMAGE_API_ADDRESS);
        StringEntity postString = new StringEntity(
                gson.toJson(new ImageRequestJSON(
                        picture.getName(),
                        picture.getExtension(),
                        ImageSize.SMALL.toString(),
                        Base64.getEncoder().encodeToString(picture.getByteData()),
                        picture.getId(),
                        picture.getPath())
                ));
        req.setEntity(postString);
        req.setHeader("Content-type", "application/json");
        HttpResponse response = httpClient.execute(req);
        picture.setId(new BasicResponseHandler().handleResponse(response));
    }

    private String postThumbnail(Thumbnail thumbnail) throws IOException {
        Gson gson = new Gson();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost req = new HttpPost(THUMBNAIL_API_ADDRESS);
        StringEntity postString = new StringEntity(
                gson.toJson(new ThumbnailRequestJSON(thumbnail.getOriginalId(), thumbnail.getSize().toString())));
        req.setEntity(postString);
        req.setHeader("Content-type", "application/json");
        HttpResponse response = httpClient.execute(req);
        return new BasicResponseHandler().handleResponse(response);
    }
    private String getHTTP(URI uri) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet req = new HttpGet(uri);
        HttpResponse response = httpClient.execute(req);
        return new BasicResponseHandler().handleResponse(response);
    }

}
