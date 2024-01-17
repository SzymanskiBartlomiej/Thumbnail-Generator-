package agh.edu.pl.util;

import agh.edu.pl.enums.Status;
import agh.edu.pl.model.Thumbnail;
import com.google.gson.Gson;
import javafx.scene.image.Image;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.TimerTask;

public class ThumbnailPollTask extends TimerTask {
    private final Thumbnail thumbnail;
    private static final String STATUS_API_ADDRESS = "http://localhost:8080/thumbnails/status";
    private static final String THUMBNAIL_API_ADDRESS = "http://localhost:8080/thumbnails/processed";

    public ThumbnailPollTask(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public void run() {
        try {
            Status status = getThumbnailStatus(thumbnail);
            if (status == Status.PROCESSED) {
                thumbnail.setThumbnailData(new Image(new ByteArrayInputStream(getThumbnailData(thumbnail))));
                thumbnail.changeStatus(Status.PROCESSED);
                cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Status getThumbnailStatus(Thumbnail thumbnail) throws IOException, URISyntaxException {
        URI uri = new URIBuilder(STATUS_API_ADDRESS).addParameter("id", thumbnail.getId()).build();
        String responseString = getHTTP(uri);
        return Status.valueOf(responseString.replace("\"", ""));
    }

    private byte[] getThumbnailData(Thumbnail thumbnail) throws IOException, URISyntaxException {
        URI uri = new URIBuilder(THUMBNAIL_API_ADDRESS).addParameter("id", thumbnail.getId()).build();
        String responseString = getHTTP(uri);
        Gson gson = new Gson();
        ImageRequestJSON responseJSON = gson.fromJson(responseString, ImageRequestJSON.class);

        return Base64.getDecoder().decode(responseJSON.getData());
    }

    private String getHTTP(URI uri) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet req = new HttpGet(uri);
        HttpResponse response = httpClient.execute(req);
        return new BasicResponseHandler().handleResponse(response);
    }
}
