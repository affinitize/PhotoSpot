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
    public static final String APIKEY = "530031de11347a205c403256e420e647";
    public static final String FORMAT_JSON = "json";

    public void getPhotos(Callback callback) {
        sendRequest("flickr.photos.getRecent", callback);
    }

    private void sendRequest(String method, Callback callback) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL).newBuilder();
        urlBuilder.addQueryParameter("method", method);
        urlBuilder.addQueryParameter("api_key", APIKEY);
        urlBuilder.addQueryParameter("format", FORMAT_JSON);
        urlBuilder.addQueryParameter("nojsoncallback", "1");

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(callback);
    }
}
