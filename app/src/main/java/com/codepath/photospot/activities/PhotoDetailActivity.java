package com.codepath.photospot.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.photospot.R;
import com.codepath.photospot.daos.FlickrPhoto;

import org.parceler.Parcels;

/**
 * Created by pauljmin on 4/16/17.
 */

public class PhotoDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FlickrPhoto photo = Parcels.unwrap(getIntent().getParcelableExtra("photo"));

        ImageView ivDetailPhoto = (ImageView) findViewById(R.id.ivDetailPhoto);
        TextView tvDetailTitle = (TextView) findViewById(R.id.tvDetailTitle);

        Glide.with(this)
                .load(photo.getUrl())
                .centerCrop()
                .into(ivDetailPhoto);

        tvDetailTitle.setText(photo.getTitle());
    }
}
