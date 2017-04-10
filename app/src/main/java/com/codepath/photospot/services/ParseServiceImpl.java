package com.codepath.photospot.services;

import android.util.Log;

import com.codepath.photospot.daos.PhotoDao;
import com.codepath.photospot.models.Photo;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by dfrie on 4/5/2017.
 */

public class ParseServiceImpl implements ParseService, ParseConstants {

    public static final String PHOTO_TYPE = "PhotoDao";

    public static final String COLUMN_NAME_URL = "url";
    public static final String COLUMN_NAME_TYPE = "type";
    public static final String COLUMN_NAME_WIDTH = "width";
    public static final String COLUMN_NAME_HEIGHT = "height";
    public static final String COLUMN_NAME_COLOR_DEPTH = "colorDepth";
    public static final String COLUMN_NAME_CATEGORIES = "categories";
    public static final String COLUMN_NAME_LIKES = "likes";
    public static final String COLUMN_NAME_DISLIKES = "dislikes";
    public static final String COLUMN_NAME_CREATED_BY = "createdBy";
    public static final String COLUMN_NAME_SOURCE = "source";
    public static final String COLUMN_NAME_CREATED_TIME = "createdTime";

    @Override
    public ServiceResponse<List<Photo>> getPhotos(HashMap<String, Object> params) {
        final ServiceResponse<List<Photo>> response = new ServiceResponse<>();
        float longitude = 0f;
        float latitude = 0f;
        float radius = 0f;
        ParseQuery<PhotoDao> query = ParseQuery.getQuery(PHOTO_TYPE);
        Set<String> keys = params.keySet();
        for (String key: keys) {
            Object value = params.get(key);
            if (key.equals(SEARCH_KEY_URL)) {
                query.whereEqualTo(COLUMN_NAME_URL, value);
            } else if (key.equals(SEARCH_KEY_LONGITUDE)) {
                longitude = (Float)value;
            } else if (key.equals(SEARCH_KEY_LATITUDE)) {
                latitude = (Float)value;
            } else if (key.equals(SEARCH_KEY_RADIUS)) {
                radius = (Float)value;
            } else if (key.equals(SEARCH_KEY_TYPE)) {
                query.whereEqualTo(COLUMN_NAME_TYPE, value);
            } else if (key.equals(SEARCH_KEY_MAX_WIDTH)) {
                query.whereLessThanOrEqualTo(COLUMN_NAME_WIDTH, value);
            } else if (key.equals(SEARCH_KEY_MIN_WIDTH)) {
                query.whereGreaterThanOrEqualTo(COLUMN_NAME_WIDTH, value);
            } else if (key.equals(SEARCH_KEY_MAX_HEIGHT)) {
                query.whereLessThanOrEqualTo(COLUMN_NAME_HEIGHT, value);
            } else if (key.equals(SEARCH_KEY_MIN_HEIGHT)) {
                query.whereGreaterThanOrEqualTo(COLUMN_NAME_HEIGHT, value);
            } else if (key.equals(SEARCH_KEY_MAX_COLORS)) {
                query.whereLessThanOrEqualTo(COLUMN_NAME_COLOR_DEPTH, value);
            } else if (key.equals(SEARCH_KEY_MIN_COLORS)) {
                query.whereGreaterThanOrEqualTo(COLUMN_NAME_COLOR_DEPTH, value);
            } else if (key.equals(SEARCH_KEY_CATEGORY)) {
                // This will Find objects where the array in categories contains the value...
                query.whereEqualTo(COLUMN_NAME_CATEGORIES, value);
            } else if (key.equals(SEARCH_KEY_MAX_LIKES)) {
                query.whereLessThanOrEqualTo(COLUMN_NAME_LIKES, value);
            } else if (key.equals(SEARCH_KEY_MIN_LIKES)) {
                query.whereGreaterThanOrEqualTo(COLUMN_NAME_LIKES, value);
            } else if (key.equals(SEARCH_KEY_MAX_DISLIKES)) {
                query.whereLessThanOrEqualTo(COLUMN_NAME_DISLIKES, value);
            } else if (key.equals(SEARCH_KEY_MIN_DISLIKES)) {
                query.whereGreaterThanOrEqualTo(COLUMN_NAME_DISLIKES, value);
            } else if (key.equals(SEARCH_KEY_CREATED_BY)) {
                query.whereEqualTo(COLUMN_NAME_CREATED_BY, value);
            } else if (key.equals(SEARCH_KEY_SOURCE)) {
                query.whereEqualTo(COLUMN_NAME_SOURCE, value);
            } else if (key.equals(SEARCH_KEY_MAX_TIME)) {
                query.whereLessThanOrEqualTo(COLUMN_NAME_CREATED_TIME, value);
            } else if (key.equals(SEARCH_KEY_MIN_TIME)) {
                query.whereGreaterThanOrEqualTo(COLUMN_NAME_CREATED_TIME, value);
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
                response.addMessage(ServiceResponse.ResponseCode.BAD_REQUEST,
                        "Unknown parameter key: " + key,
                        ServiceResponse.MessageSeverity.ERROR);
            }
        }

        if ((longitude!=0 || latitude!=0 || radius!=0) &&
                (latitude==0 || longitude==0 || radius==0)) {
            response.addMessage(ServiceResponse.ResponseCode.BAD_REQUEST,
                    "For proximity, you must supply all 3 parameters for latitude, longitude and radius.",
                    ServiceResponse.MessageSeverity.ERROR);
        }

        // TODO: implement the location filter...
        // ...

        if (response.getMessages().size()==0) {
            query.findInBackground(new FindCallback<PhotoDao>() {
                public void done(List<PhotoDao> photoDaoList, ParseException e) {
                    if (e == null) {
                        Log.d("photo", "Retrieved " + photoDaoList.size() + " photos");
                        List<Photo> photoList = new ArrayList<Photo>();
                        for (int i=0; i<photoDaoList.size(); i++) {
                            photoList.add(new Photo(photoDaoList.get(i)));
                        }
                        response.set(photoList);
                    } else {
                        Log.d("photo", "Error: " + e.getMessage());
                        response.addMessage(ServiceResponse.ResponseCode.SERVER_ERROR,
                                "Error while getting list: " + e.getMessage(),
                                ServiceResponse.MessageSeverity.ERROR);
                    }
                }
            });
        }
        return response;
    }

    @Override
    public ServiceResponse<Photo> postPhoto(final Photo photo) {
        final ServiceResponse<Photo> response = new ServiceResponse<>();
        response.set(photo);
        PhotoDao photoDao = PhotoDao.newPhotoDao(photo);
        try {
            photoDao.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    response.setStatusCode(ServiceResponse.ResponseCode.OK);
                }
            });
        } catch (Exception e) {
            response.setStatusCode(ServiceResponse.ResponseCode.SERVER_ERROR);
            response.addMessage(ServiceResponse.ResponseCode.SERVER_ERROR, e.getMessage(),
                    ServiceResponse.MessageSeverity.ERROR);
        }
        return response;
    }

    @Override
    public ServiceResponse<Photo> updatePhoto(Photo photo) {
        return postPhoto(photo);
    }

    @Override
    public ServiceResponse<Photo> deletePhoto(Photo photo) {
        final ServiceResponse<Photo> response = new ServiceResponse<>();
        response.set(photo);
        PhotoDao photoDao = PhotoDao.newPhotoDao(photo);
        try {
            photoDao.deleteInBackground(new DeleteCallback() {
                @Override
                public void done(ParseException e) {
                    response.setStatusCode(ServiceResponse.ResponseCode.OK);
                }
            });
        } catch (Exception e) {
            response.setStatusCode(ServiceResponse.ResponseCode.SERVER_ERROR);
            response.addMessage(ServiceResponse.ResponseCode.SERVER_ERROR, e.getMessage(),
                    ServiceResponse.MessageSeverity.ERROR);
        }
        return response;
    }

    @Override
    public void likePhoto(Photo photo) {
        incrementPhotoColumn(photo, COLUMN_NAME_LIKES);
    }

    @Override
    public void dislikePhoto(Photo photo) {
        incrementPhotoColumn(photo, COLUMN_NAME_DISLIKES);
    }

    private void incrementPhotoColumn(Photo photo, final String column) {
        // Retrieve the object by id to refresh...
        ParseQuery<PhotoDao> query = ParseQuery.getQuery(PHOTO_TYPE);
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
