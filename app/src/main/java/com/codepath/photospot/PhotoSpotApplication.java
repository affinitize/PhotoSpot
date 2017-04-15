package com.codepath.photospot;

import android.app.Application;
import android.widget.Toast;

import com.codepath.photospot.daos.PhotoDao;
import com.codepath.photospot.network.FlickrClient;
import com.parse.Parse;
import com.parse.ParseObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Created by dfrie on 4/5/2017.
 */

public class PhotoSpotApplication extends Application {

    public static final String API_KEY_FILE = "api_key.properties";
    public static final String APP_ID_KEY = "parse_app_id";
    public static String APP_ID_SECRET;
    public static final String SERVER_URL_KEY = "parse_server_url";
    public static String SERVER_URL_SECRET;
    public static final String MASTER_KEY_KEY = "parse_master_key";
    public static String MASTER_KEY_SECRET;

    private static final String FLICKR_API_KEY = "flickr_api_key";
    FlickrClient flickrClient;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            InputStream is = getBaseContext().getAssets().open(API_KEY_FILE);
            Properties props = new Properties();
            props.load(is);
            APP_ID_SECRET = props.getProperty(APP_ID_KEY);
            SERVER_URL_SECRET = props.getProperty(SERVER_URL_KEY);
            MASTER_KEY_SECRET = props.getProperty(MASTER_KEY_KEY);
            String flickrApiKey = props.getProperty(FLICKR_API_KEY);
            flickrClient = new FlickrClient(flickrApiKey);
        } catch (FileNotFoundException e) {
            Toast.makeText(this, API_KEY_FILE + getString(R.string.file_not_found), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Register your parse models here
        ParseObject.registerSubclass(PhotoDao.class);

        // Existing initialization happens after all classes are registered

        // set applicationId and server based on the values in the Heroku settings.
        // any network interceptors must be added with the Configuration Builder given this syntax

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(APP_ID_SECRET) // should correspond to APP_ID env variable
/*
                .addNetworkInterceptor(new ParseLogInterceptor())
*/
                .server(SERVER_URL_SECRET).build());

    }

    public FlickrClient getFlickrClient() {
        return flickrClient;
    }
}
