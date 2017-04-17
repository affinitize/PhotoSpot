package com.codepath.photospot.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.photospot.R;
import com.codepath.photospot.activities.PhotoDetailActivity;
import com.codepath.photospot.adapters.PhotoAdapter;
import com.codepath.photospot.daos.FlickrPhoto;
import com.codepath.photospot.utils.ItemClickSupport;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pauljmin on 4/8/17.
 */

public class PhotoListFragment extends Fragment {
    ArrayList<FlickrPhoto> photos;
    PhotoAdapter adapter;

    public static PhotoListFragment newInstance() {
        PhotoListFragment plf = new PhotoListFragment();
        return plf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_list, parent, false);
        RecyclerView rvPhotos = (RecyclerView) v.findViewById(R.id.rvPhotos);
        rvPhotos.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvPhotos.setLayoutManager(linearLayoutManager);
        setHasOptionsMenu(true);

        ItemClickSupport.addTo(rvPhotos).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent i = new Intent(getActivity(), PhotoDetailActivity.class);
                        FlickrPhoto photo = photos.get(position);
                        i.putExtra("photo", Parcels.wrap(photo));
                        startActivity(i);
                    }
                }
        );
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photos = new ArrayList<>();
        adapter = new PhotoAdapter(getActivity(), photos);
    }

    public void addAll(List<FlickrPhoto> photos) {
        this.photos.addAll(photos);
        adapter.notifyDataSetChanged();
    }

    public void newList(List<FlickrPhoto> photos) {
        this.photos.clear();
        this.photos.addAll(photos);
        adapter.notifyDataSetChanged();
    }
}
