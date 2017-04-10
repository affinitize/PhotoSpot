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
import com.codepath.photospot.network.FlickrClient;
import com.codepath.photospot.network.FlickrSearchResponse;

import java.io.IOException;
import java.util.ArrayList;
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

    public void photoSearch(double lat, double lang) {
        photos.clear();
        FlickrClient fc = FlickrClient.getFlickrClient();
        fc.getPhotos(cb, lat, lang);
    }

    Callback cb = new Callback() {
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

}
