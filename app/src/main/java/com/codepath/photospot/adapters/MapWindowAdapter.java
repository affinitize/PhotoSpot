package com.codepath.photospot.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.photospot.R;
import com.codepath.photospot.daos.FlickrPhoto;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by pauljmin on 4/15/17.
 */

public class MapWindowAdapter implements GoogleMap.InfoWindowAdapter {
    LayoutInflater mInflater;
    Activity activity;

    public MapWindowAdapter(LayoutInflater i, Activity activity){
        mInflater = i;
        this.activity = activity;
    }

    // This defines the contents within the info window based on the marker
    @Override
    public View getInfoContents(Marker marker) {
        // Getting view from the layout file
        View v = mInflater.inflate(R.layout.map_photo_window, null);

        FlickrPhoto photo = (FlickrPhoto) marker.getTag();
        // Populate fields
        TextView tvMapWindowTitle = (TextView) v.findViewById(R.id.tv_map_window_title);
        tvMapWindowTitle.setText(marker.getTitle());

        ImageView ivMapWindowPhoto = (ImageView) v.findViewById(R.id.iv_map_window_photo);

        Picasso.with(activity)
                .load(photo.getUrl())
                .into(ivMapWindowPhoto, new MarkerCallback(marker));

        return v;
    }

    // This changes the frame of the info window; returning null uses the default frame.
    // This is just the border and arrow surrounding the contents specified above
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    public class MarkerCallback implements Callback {
        Marker marker=null;

        MarkerCallback(Marker marker) {
            this.marker=marker;
        }

        @Override
        public void onError() {
            Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
        }

        @Override
        public void onSuccess() {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
        }
    }
}