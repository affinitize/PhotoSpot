package com.codepath.photospot.services;

import com.codepath.photospot.daos.PhotoDao;
import com.codepath.photospot.models.Photo;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by dfrie on 4/5/2017.
 */

public class ParseServiceImpl implements ParseService, ParseConstants {

    public static final String PHOTO_QUERY_TYPE = "PhotoDao";

    private static ParseServiceImpl instance;

    double longitude = 0d;
    double latitude = 0d;
    int radius = 0;

    /**
     * Singleton getter
     * @return ParseServiceImpl
     */
    public static ParseServiceImpl getInstance() {
        if (instance == null) {
            instance = new ParseServiceImpl();
        }
        return instance;
    }

    /**
     * Singleton constructor is private
     */
    private ParseServiceImpl() {
        super();
    }

    @Override
    public void getPhotos(HashMap<String, Object> params, FindCallback<PhotoDao> callback) {
        longitude = 0d;
        latitude = 0d;
        radius = 0;
        ParseQuery<PhotoDao> query = ParseQuery.getQuery(PHOTO_QUERY_TYPE);
        Set<String> keys = params.keySet();
        for (String key: keys) {
            Object value = params.get(key);
            if (key.equals(SEARCH_KEY_URL)) {
                query.whereEqualTo(PhotoDao.URL_KEY, value);
            } else if (key.equals(SEARCH_KEY_LONGITUDE)) {
                longitude = (Double)value;
            } else if (key.equals(SEARCH_KEY_LATITUDE)) {
                latitude = (Double)value;
            } else if (key.equals(SEARCH_KEY_RADIUS)) {
                radius = (Integer)value;
            } else if (key.equals(SEARCH_KEY_TYPE)) {
                query.whereEqualTo(PhotoDao.TYPE_KEY, value);
            } else if (key.equals(SEARCH_KEY_MAX_WIDTH)) {
                query.whereLessThanOrEqualTo(PhotoDao.WIDTH_KEY, value);
            } else if (key.equals(SEARCH_KEY_MIN_WIDTH)) {
                query.whereGreaterThanOrEqualTo(PhotoDao.WIDTH_KEY, value);
            } else if (key.equals(SEARCH_KEY_MAX_HEIGHT)) {
                query.whereLessThanOrEqualTo(PhotoDao.HEIGHT_KEY, value);
            } else if (key.equals(SEARCH_KEY_MIN_HEIGHT)) {
                query.whereGreaterThanOrEqualTo(PhotoDao.HEIGHT_KEY, value);
            } else if (key.equals(SEARCH_KEY_MAX_COLORS)) {
                query.whereLessThanOrEqualTo(PhotoDao.COLOR_DEPTH_KEY, value);
            } else if (key.equals(SEARCH_KEY_MIN_COLORS)) {
                query.whereGreaterThanOrEqualTo(PhotoDao.COLOR_DEPTH_KEY, value);
            } else if (key.equals(SEARCH_KEY_CATEGORY)) {
                // This will Find objects where the array in categories contains the value...
                query.whereEqualTo(PhotoDao.CATEGORIES_KEY, value);
            } else if (key.equals(SEARCH_KEY_MAX_LIKES)) {
                query.whereLessThanOrEqualTo(PhotoDao.LIKES_KEY, value);
            } else if (key.equals(SEARCH_KEY_MIN_LIKES)) {
                query.whereGreaterThanOrEqualTo(PhotoDao.LIKES_KEY, value);
            } else if (key.equals(SEARCH_KEY_MAX_DISLIKES)) {
                query.whereLessThanOrEqualTo(PhotoDao.DISLIKES_KEY, value);
            } else if (key.equals(SEARCH_KEY_MIN_DISLIKES)) {
                query.whereGreaterThanOrEqualTo(PhotoDao.DISLIKES_KEY, value);
            } else if (key.equals(SEARCH_KEY_CREATED_BY)) {
                query.whereEqualTo(PhotoDao.CREATED_BY_KEY, value);
            } else if (key.equals(SEARCH_KEY_SOURCE)) {
                query.whereEqualTo(PhotoDao.SOURCE_KEY, value);
            } else if (key.equals(SEARCH_KEY_MAX_TIME)) {
                query.whereLessThanOrEqualTo(PhotoDao.CREATED_TIME_KEY, value);
            } else if (key.equals(SEARCH_KEY_MIN_TIME)) {
                query.whereGreaterThanOrEqualTo(PhotoDao.CREATED_TIME_KEY, value);
            } else if (key.equals(SEARCH_KEY_SKIP_COUNT)) {
                // start at the offset value...
                query.setSkip((Integer)value);
            } else if (key.equals(SEARCH_KEY_MAX_RESULTS)) {
                // limit to at most x results...
                query.setLimit((Integer)value);
            } else if (key.equals(SEARCH_KEY_ORDER_BY)) {
                // set the order...
                query.orderByDescending((String)value);
            } else {
                throw new IllegalArgumentException(
                        "Error: Unknown parameter key: " + key);
            }
        }

        if ((longitude!=0 || latitude!=0 || radius!=0) &&
                (latitude==0 || longitude==0 || radius==0)) {
            throw new IllegalArgumentException(
                    "For proximity, you must supply all 3 parameters for latitude, longitude and radius.");
        }

        // Simple Location filter without using the problematic whereNear() method...
        if (radius!=0) {
            double degrees = radius/70d;
            query.whereGreaterThan(PhotoDao.LONGITUDE_KEY, longitude - degrees);
            query.whereLessThan(PhotoDao.LONGITUDE_KEY, longitude + degrees);
            query.whereGreaterThan(PhotoDao.LATITUDE_KEY, latitude - degrees);
            query.whereLessThan(PhotoDao.LATITUDE_KEY, latitude + degrees);
        }

        query.findInBackground(callback);
    }

    @Override
    public void postPhoto(final Photo photo, SaveCallback callback) {
        PhotoDao photoDao = PhotoDao.newPhotoDao(photo);
        photoDao.saveInBackground(callback);
    }

    @Override
    public void updatePhoto(Photo photo, SaveCallback callback) {
        postPhoto(photo, callback);
    }

    @Override
    public void deletePhoto(Photo photo, DeleteCallback callback) {
        PhotoDao photoDao = PhotoDao.newPhotoDao(photo);
        photoDao.deleteInBackground(callback);
    }

    @Override
    public void likePhoto(Photo photo) {
        incrementPhotoColumn(photo, PhotoDao.LIKES_KEY);
    }

    @Override
    public void dislikePhoto(Photo photo) {
        incrementPhotoColumn(photo, PhotoDao.DISLIKES_KEY);
    }

    private void incrementPhotoColumn(Photo photo, final String column) {
        // Retrieve the object by id to refresh...
        ParseQuery<PhotoDao> query = ParseQuery.getQuery(PHOTO_QUERY_TYPE);
        query.getInBackground(photo.getObjectId(), new GetCallback<PhotoDao>() {
            public void done(PhotoDao photoDao, ParseException e) {
                if (e == null) {
                    // Now let's update it...
                    int newcount = photoDao.getInt(column) + 1;
                    photoDao.put(column, newcount);
                    ///photoDao.saveEventually();
                    photoDao.saveInBackground();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

}
