package com.codepath.photospot.network;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by pauljmin on 4/8/17.
 */

public class FlickrClient {
    public static final String URL = "https://api.flickr.com/services/rest/";
    public static final String FORMAT_JSON = "json";

    private String apiKey;

    /**
     * constructor needs to be private for singleton
     */
    public FlickrClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public void getPhotos(Callback callback, double lat, double lang) {
        sendRequest("flickr.photos.search", callback, lat, lang);
    }

    private void sendRequest(String method, Callback callback, double lat, double lang) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL).newBuilder();
        urlBuilder.addQueryParameter("method", method);
        urlBuilder.addQueryParameter("api_key", apiKey);
        urlBuilder.addQueryParameter("format", FORMAT_JSON);
        urlBuilder.addQueryParameter("nojsoncallback", "1");
        urlBuilder.addQueryParameter("extras", "description,date_upload,owner_name,geo,tags,url_m");
        urlBuilder.addQueryParameter("content_type", "1");
        urlBuilder.addQueryParameter("per_page", "20");

        urlBuilder.addQueryParameter("lat", String.valueOf(lat));
        urlBuilder.addQueryParameter("lon", String.valueOf(lang));
        urlBuilder.addQueryParameter("tags",
                "sunset,beach,water,sky,nature,night,art,light,snow,sun,clouds,park,winter,landscape,summer,sea,city,lake,bridge");

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(callback);
    }
}
