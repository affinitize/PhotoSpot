package com.codepath.photospot.services;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by pauljmin on 4/9/17.
 */

public class LocationService implements GoogleApiClient.ConnectionCallbacks,
                                           GoogleApiClient.OnConnectionFailedListener {

    private static final int PERMISSIONS_REQUEST_CODE = 25;

    public interface LocationUpdateListener {
        void onLocationUpdate(Location location);
    }

    private GoogleApiClient mGoogleApiClient;
    private Activity activity;

    public LocationService(Activity activity) {
        this.activity = activity;
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void connect() {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    PERMISSIONS_REQUEST_CODE);
        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation == null) {
            // toast an error message
        }

        LocationUpdateListener listener = (LocationUpdateListener) activity;
        listener.onLocationUpdate(mLastLocation);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
