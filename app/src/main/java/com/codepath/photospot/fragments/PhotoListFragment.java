package com.codepath.photospot.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.photospot.R;
import com.codepath.photospot.adapters.PhotoAdapter;
import com.codepath.photospot.daos.FlickrPhoto;
import com.codepath.photospot.daos.FlickrPhotos;
import com.codepath.photospot.daos.PhotoDao;
import com.codepath.photospot.models.Photo;
import com.codepath.photospot.network.FlickrClient;
import com.codepath.photospot.network.FlickrSearchResponse;
import com.codepath.photospot.services.ParseServiceImpl;
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
 * Created by pauljmin on 4/8/17.
 */

public class PhotoListFragment extends Fragment {
    ArrayList<FlickrPhoto> photos;
    PhotoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_list, parent, false);
        RecyclerView rvPhotos = (RecyclerView) v.findViewById(R.id.rvPhotos);
        rvPhotos.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvPhotos.setLayoutManager(linearLayoutManager);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photos = new ArrayList<>();
        adapter = new PhotoAdapter(getActivity(), photos);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }

    public void photoSearch(double latit, double longit) {
        photos.clear();
        FlickrClient fc = FlickrClient.getFlickrClient();
        fc.getPhotos(flickrCallback, latit, longit);

        // Piggybacking on the existing call we need to merge results with,...
        HashMap<String, Object> params = new HashMap<>();
        params.put(ParseServiceImpl.SEARCH_KEY_LONGITUDE, longit);
        params.put(ParseServiceImpl.SEARCH_KEY_LATITUDE, latit);
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addAll(photos.photo);
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

    public void addAll(List<FlickrPhoto> photos) {
        this.photos.addAll(photos);
        adapter.notifyDataSetChanged();
    }

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
