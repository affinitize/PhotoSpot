package com.codepath.photospot.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.codepath.photospot.R;
import com.codepath.photospot.fragments.PhotoListFragment;
import com.codepath.photospot.services.LocationService;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements LocationService.LocationUpdateListener {

    private PhotoListFragment photoListFragment;
    private LocationService locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (savedInstanceState == null) {
            photoListFragment = (PhotoListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_search_results);

        }
        locationService = new LocationService(MainActivity.this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i("DEBUG", "Place: " + place.getName());
                LatLng latLng = place.getLatLng();
                photoListFragment.photoSearch(latLng.latitude, latLng.longitude);
            }

            @Override
            public void onError(Status status) {
                Log.i("DEBUG", "An error occurred: " + status);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top, menu);
        return true;
    }

    protected void onStart() {
        locationService.connect();
        super.onStart();
    }

    public void onLocationUpdate(Location location) {
        this.photoListFragment.photoSearch(location.getLatitude(), location.getLongitude());
    }
}
