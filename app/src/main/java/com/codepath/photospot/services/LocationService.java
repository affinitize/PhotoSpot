package com.codepath.photospot.services;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
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

        if (mLastLocation==null) {
            // This is the location for the Netscape building in Los Gatos...
            mLastLocation = new Location("APP");
            mLastLocation.setLatitude(37.25704429);
            mLastLocation.setLongitude(-121.96378827);
            mLastLocation.setAltitude(85.22966003);
            //mLastLocation.makeComplete();
            mLastLocation.setProvider("?");
            mLastLocation.setAccuracy(100.0f);
            mLastLocation.setTime(System.currentTimeMillis());
            mLastLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
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
