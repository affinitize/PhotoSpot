package com.codepath.photospot.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.photospot.R;
import com.codepath.photospot.daos.FlickrPhoto;
import com.codepath.photospot.fragments.PhotoMapFragment;
import com.codepath.photospot.fragments.PhotoListFragment;
import com.codepath.photospot.services.LocationService;
import com.codepath.photospot.services.PhotoSearch;
import com.codepath.photospot.utils.SmartFragmentStatePagerAdapter;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity
                          implements LocationService.LocationUpdateListener,
                                     PhotoSearch.PhotoSearchListener {

    private LocationService locationService;
    private ViewPager vpSearchResults;
    private SmartFragmentStatePagerAdapter adapterViewPager;

    /**
     * Used for searching by Google Place
     */
    private PlaceSelectionListener psl = new PlaceSelectionListener() {
        @Override
        public void onPlaceSelected(Place place) {
            Log.i("DEBUG", "Place: " + place.getName());
            LatLng latLng = place.getLatLng();
            PhotoSearch ps = new PhotoSearch(MainActivity.this, latLng.latitude, latLng.longitude);
            ps.executeSearch();
        }

        @Override
        public void onError(Status status) {
            Log.i("DEBUG", "An error occurred: " + status);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Display icon in the toolbar
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // set up the viewpage to handle the list view and map view for search results
        vpSearchResults = (ViewPager) findViewById(R.id.vpSearchResults);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpSearchResults.setAdapter(adapterViewPager);

        locationService = new LocationService(MainActivity.this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(psl);
    }

    @Override
    protected void onStart() {
        locationService.connect();
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        locationService = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_add:
                Toast.makeText(this, "Add was selected...", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), AddPhotoActivity.class);
                startActivity(i);
                return true;
            case R.id.action_search:
                Toast.makeText(this, "Search was selected...", Toast.LENGTH_LONG).show();
                i = new Intent(getApplicationContext(), AddPhotoActivity.class);
                startActivity(i);
                return true;
            case R.id.action_toggle_view:
                if (vpSearchResults.getCurrentItem() == 0) {
                    vpSearchResults.setCurrentItem(1);
                } else {
                    vpSearchResults.setCurrentItem(0);
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Runs search with given location
     * @param location new location
     */
    public void onLocationUpdate(Location location) {
        PhotoSearch ps = new PhotoSearch(MainActivity.this, location.getLatitude(), location.getLongitude());
        ps.executeSearch();
    }

    /**
     * Used to update both search results fragments
     * @param photos List of photos that was generated from the search
     */
    public void useSearchResults(List<FlickrPhoto> photos) {
        PhotoListFragment fragment = (PhotoListFragment) adapterViewPager.getRegisteredFragment(0);
        fragment.newList(photos);
    }

    /**
     * adapter for the view pager
     */
    private static class MyPagerAdapter extends SmartFragmentStatePagerAdapter {
        private static int NUM_ITEMS = 2;

        private MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PhotoListFragment.newInstance();
                case 1:
                    return PhotoMapFragment.newInstance();
                default:
                    return null;
            }
        }
    }
}
