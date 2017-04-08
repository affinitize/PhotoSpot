package com.codepath.photospot.network;

import com.codepath.photospot.daos.FlickrPhotos;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by pauljmin on 4/8/17.
 */

public class FlickrSearchResponse {
    public FlickrPhotos photos;

    // public constructor is necessary for collections
    public FlickrSearchResponse() {}

    public static FlickrSearchResponse parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        FlickrSearchResponse flickrSearchResponse = gson.fromJson(response, FlickrSearchResponse.class);
        return flickrSearchResponse;
    }
}
