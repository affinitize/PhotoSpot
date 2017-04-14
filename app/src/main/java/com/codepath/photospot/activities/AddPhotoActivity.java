package com.codepath.photospot.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.photospot.R;
import com.codepath.photospot.models.Photo;
import com.codepath.photospot.services.LocationService;
import com.codepath.photospot.services.ParseServiceImpl;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.Date;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by dfrie on 4/8/2017.
 */
public class AddPhotoActivity extends AppCompatActivity implements LocationService.LocationUpdateListener {

    public final static String APP_SOURCE = "PhotoSpot";

    private ImageView ivPhoto;
    private EditText etUrl;
    private EditText etComment;
    private EditText etCategories;
    private EditText etCreatedBy;
    private Button btAdd;
    private TextView tvErrorMsg;

    private LocationService locationService;
    private double latitude = Double.MAX_VALUE;
    private double longitude = Double.MAX_VALUE;
    private String placeName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        //Toast.makeText(this.getApplicationContext(), "...in activity_add_photo...", Toast.LENGTH_SHORT).show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Display icon in the toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        // diable the default toolbar title...
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        etUrl = (EditText) findViewById(R.id.etUrl);
        etComment = (EditText) findViewById(R.id.etComment);
        etCategories = (EditText) findViewById(R.id.etCategories);
        etCreatedBy = (EditText) findViewById(R.id.etCreatedBy);
        btAdd = (Button) findViewById(R.id.btAddPhoto);
        tvErrorMsg = (TextView) findViewById(R.id.tvErrorMsg);

        locationService = new LocationService(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i("DEBUG", "Place: " + place.getName());
                LatLng latLng = place.getLatLng();
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                placeName = place.getName().toString();
            }

            @Override
            public void onError(Status status) {
                Log.i("DEBUG", "An error occurred getting Place: " + status);
            }
        });


        etUrl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String url = etUrl.getText().toString();
                    if (url.length()>10 && url.startsWith("http")) {
                        Picasso.with(AddPhotoActivity.this).load(url)
                               /* .placeholder(R.drawable.loading)*/
                                .transform(new RoundedCornersTransformation(28, 28))
                                .into(ivPhoto);
                    }
                }
            }
        });

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = etUrl.getText().toString();
                if (url.length() == 0) {
                    tvErrorMsg.setText(R.string.enter_a_url);
                    return;
                }
                if (latitude == Double.MAX_VALUE || longitude == Double.MAX_VALUE ||
                        placeName == null) {
                    tvErrorMsg.setText(R.string.enter_a_location);
                    return;
                }
                int pos = url.lastIndexOf('.');
                String type = pos>0? url.substring(pos + 1).toUpperCase(): "";
                Photo photo = new Photo();
                photo.setType(type);
                photo.setUrl(url);

                photo.setComment(etComment.getText().toString());
                photo.setCategories(etCategories.getText().toString());
                photo.setCreatedby(etCreatedBy.getText().toString());
                photo.setLatitude(latitude);
                photo.setLongitude(longitude);
                photo.setSource(APP_SOURCE);
                photo.setCreatedtime(new Date().getTime());
                photo.setCategories(placeName.replaceAll(" ", ""));
                photo.setLikes(1);

                SaveCallback callback = new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(AddPhotoActivity.this.getApplicationContext(),
                                "Photo has been saved.", Toast.LENGTH_LONG).show();
                    }
                };
                ParseServiceImpl.getInstance().postPhoto(photo, callback);

                Intent i = new Intent(AddPhotoActivity.this.getApplicationContext(),
                        MainActivity.class);
                startActivity(i);
             }
        });
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
    public void onLocationUpdate(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        //placeName = location.toString();
    }
}
