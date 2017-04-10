package com.codepath.photospot.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.codepath.photospot.R;
import com.codepath.photospot.fragments.PhotoListFragment;
import com.codepath.photospot.services.LocationService;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements LocationService.LocationUpdateListener {

    private PhotoListFragment photoListFragment;
    private LocationService locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            photoListFragment = (PhotoListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_search_results);

        }
        locationService = new LocationService(MainActivity.this);
    }

    protected void onStart() {
        locationService.connect();
        super.onStart();
    }

    public void onLocationUpdate(Location location) {
        this.photoListFragment.photoSearch(location);
    }
}
