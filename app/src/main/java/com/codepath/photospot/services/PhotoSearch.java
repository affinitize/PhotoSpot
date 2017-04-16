package com.codepath.photospot.services;

import android.app.Activity;
import android.util.Log;

import com.codepath.photospot.PhotoSpotApplication;
import com.codepath.photospot.daos.FlickrPhoto;
import com.codepath.photospot.daos.FlickrPhotos;
import com.codepath.photospot.daos.PhotoDao;
import com.codepath.photospot.models.Photo;
import com.codepath.photospot.network.FlickrClient;
import com.codepath.photospot.network.FlickrSearchResponse;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by pauljmin on 4/15/17.
 */

public class PhotoSearch {

    public interface PhotoSearchListener {
        void useSearchResults(List<FlickrPhoto> photos);
    }

    Activity activity;
    private double latitude;
    private double longitude;

    public PhotoSearch(Activity activity, double latitude, double longitude) {
        this.activity = activity;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void executeSearch() {
        PhotoSpotApplication psa = (PhotoSpotApplication) activity.getApplication();
        FlickrClient fc = psa.getFlickrClient();
        fc.getPhotos(flickrCallback, latitude, longitude);

        // Piggybacking on the existing call we need to merge results with,...
        HashMap<String, Object> params = new HashMap<>();
        params.put(ParseServiceImpl.SEARCH_KEY_LONGITUDE, longitude);
        params.put(ParseServiceImpl.SEARCH_KEY_LATITUDE, latitude);
        params.put(ParseServiceImpl.SEARCH_KEY_RADIUS, 500);
        params.put(ParseServiceImpl.SEARCH_KEY_MAX_RESULTS, 20);
        ParseServiceImpl.getInstance().getPhotos(params, parseCallback);
    }

    Callback flickrCallback = new Callback() {
        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            try {
                String responseData = response.body().string();
                Log.d("api response", responseData);
                FlickrSearchResponse fsr = FlickrSearchResponse.parseJSON(responseData);
                final FlickrPhotos photos = fsr.photos;
                if (response.isSuccessful()) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((PhotoSearchListener)activity).useSearchResults(photos.photo);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }
    };

    FindCallback<PhotoDao> parseCallback = new FindCallback<PhotoDao>() {
        public void done(List<PhotoDao> photoDaoList, ParseException e) {
            if (e == null) {
                Log.d("parsePhotos", "Retrieved " + photoDaoList.size() + " photos");
                List<Photo> photoList = new ArrayList<Photo>();
                for (int i=0; i<photoDaoList.size(); i++) {
                    photoList.add(new Photo(photoDaoList.get(i)));
                }
                //do some stuff with (photoList);
            } else {
                Log.d("parsePhotos", "Error while getting list: " + e.getMessage());
                e.printStackTrace();
            }
        }
    };
}
