package com.codepath.photospot.daos;

import com.codepath.photospot.models.Photo;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;

/**
 * Data object for a Photo to be used with Parse Db.
 *
 * { "123456789": {
 *     "url": "http://my.site.com/mypix/1234.gif",
 *     "url": "http://my.site.com/mypix/1234.gif",
 *     "width": 640,
 *     "height": 480,
 * 	   "type": "GIF",
 *     "colordepth": 256,
 *     "longitude": 157.123,
 *     "latitude": -112.512,
 *     "categories": ["building", "city"],
 *     "likes": 1021,
 *     "dislikes": 1,
 *     "comment": "This is a picture",
 *     "createdby": "PaulMin999",
 *     "createdtime": 123456789,
 *     "source": "Flickr"
 *   },...
 * }
 *
 * @author dfriedman
 *
 */
@ParseClassName("PhotoDao")
public class PhotoDao extends ParseObject {

    // This is the internal parseDb unique id
    public static final String OBJECT_ID_KEY = "objectId";
    public static final String URL_KEY = "url";
    public static final String WIDTH_KEY = "width";
    public static final String HEIGHT_KEY = "height";
    public static final String COLOR_DEPTH_KEY = "colorDepth";
    public static final String TYPE_KEY = "type";
    public static final String LONGITUDE_KEY = "longitude";
    public static final String LATITUDE_KEY = "latitude";
    public static final String CATEGORIES_KEY = "categories";
    public static final String LIKES_KEY = "likes";
    public static final String DISLIKES_KEY = "dislikes";
    public static final String COMMENT_KEY = "comment";
    public static final String CREATED_BY_KEY = "createdBy";
    public static final String CREATED_TIME_KEY = "createdTime";
    public static final String SOURCE_KEY = "source";

    /*
     * Ensure that your subclass has a public default (i.e. zero-argument) constructor.
     */
    public PhotoDao() {
    }

    public static synchronized PhotoDao newPhotoDao(Photo photo) {
        PhotoDao photoDao;
        if (photo==null || photo.getObjectId()==null) {
            photoDao = ParseObject.create(PhotoDao.class);
        } else {
            photoDao = ParseObject.createWithoutData(PhotoDao.class, photo.getObjectId());
        }
        photoDao.setDataFromPhoto(photo);
        return photoDao;
    }

    public void setDataFromPhoto(Photo photo) {
        setObjectId(photo.getObjectId());
        setUrl(photo.getUrl());
        setComment(photo.getComment());
        setType(photo.getType());
        setCreatedBy(photo.getCreatedby());
        setSource(photo.getSource());
        setWidth(photo.getWidth());
        setHeight(photo.getHeight());
        setColorDepth(photo.getColordepth());
        setLikes(photo.getLikes());
        setDislikes(photo.getDislikes());
        setCreatedTime(photo.getCreatedtime());
        setLongitude(photo.getLongitude());
        setLatitude(photo.getLatitude());

        String[] cats = photo.getCategories();
        JSONArray jsonArray = new JSONArray();
        for (int i=0; i<cats.length; i++) {
            jsonArray.put(cats[i]);
        }
        setCategories(jsonArray);
    }

    public String getUrl() {
        return getString(URL_KEY);
    }
    public void setUrl(String url) {
        put(URL_KEY, url);
    }
    public String getComment() {
        return getString(COMMENT_KEY);
    }
    public void setComment(String comment) {
        put(COMMENT_KEY, comment);
    }
    public String getType() {
        return getString(TYPE_KEY);
    }
    public void setType(String type) {
        put(TYPE_KEY, type);
    }
    public String getCreatedBy() {
        return getString(CREATED_BY_KEY);
    }
    public void setCreatedBy(String createdBy) {
        put(CREATED_BY_KEY, createdBy);
    }
    public String getSource() {
        return getString(SOURCE_KEY);
    }
    public void setSource(String source) {
        put(SOURCE_KEY, source);
    }

    public int getWidth() {
        return getInt(WIDTH_KEY);
    }
    public void setWidth(int width) {
        put(WIDTH_KEY, width);
    }
    public int getHeight() {
        return getInt(HEIGHT_KEY);
    }
    public void setHeight(int height) {
        put(HEIGHT_KEY, height);
    }
    public int getColorDepth() {
        return getInt(COLOR_DEPTH_KEY);
    }
    public void setColorDepth(int colorDepth) {
        put(COLOR_DEPTH_KEY, colorDepth);
    }
    public int getLikes() {
        return getInt(LIKES_KEY);
    }
    public void setLikes(int likes) {
        put(LIKES_KEY, likes);
    }
    public int getDislikes() {
        return getInt(DISLIKES_KEY);
    }
    public void setDislikes(int dislikes) {
        put(DISLIKES_KEY, dislikes);
    }

    public long getCreatedTime() {
        return getLong(CREATED_TIME_KEY);
    }
    public void setCreatedTime(long createdTime) {
        put(CREATED_TIME_KEY, createdTime);
    }

    public double getLongitude() {
        return getDouble(LONGITUDE_KEY);
    }
    public void setLongitude(double longitude) {
        put(LONGITUDE_KEY, longitude);
    }
    public double getLatitude() {
        return getDouble(LATITUDE_KEY);
    }
    public void setLatitude(double latitude) {
        put(LATITUDE_KEY, latitude);
    }

    public JSONArray getCategories() {
        return getJSONArray(CATEGORIES_KEY);
    }
    public void setCategories(JSONArray categories) {
        put(CATEGORIES_KEY, categories);
    }


}