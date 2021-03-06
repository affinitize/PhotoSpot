package com.codepath.photospot.models;

import com.codepath.photospot.daos.FlickrPhoto;
import com.codepath.photospot.daos.PhotoDao;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by dfrie on 4/6/2017.
 */

public class Photo {

    public static final String PHOTO_SOURCE_FLICKR = "Flickr";

    public static final String FLICKR_CATEGORY_FRIEND = "Friend";
    public static final String FLICKR_CATEGORY_FAMILY = "Family";
    public static final String FLICKR_CATEGORY_PUBLIC = "Public";

    public String objectId;
    public String url;
    public int width;
    public int height;
    public String type;
    public int colordepth;
    public double longitude;
    public double latitude;
    public String categories;
    public int likes;
    public int dislikes;
    public String comment;
    public String createdby;
    public long createdtime;
    public String source;

    public Photo() {
        super();
    }

    public Photo(PhotoDao dao) {
        this();

        objectId = dao.getObjectId();
        url = dao.getUrl();
        width = dao.getWidth();
        height = dao.getHeight();
        type = dao.getType();
        colordepth = dao.getColorDepth();
        longitude = dao.getLongitude();
        latitude = dao.getLatitude();
        likes = dao.getLikes();
        dislikes = dao.getDislikes();
        comment = dao.getComment();
        createdby = dao.getCreatedBy();
        createdtime = dao.getCreatedTime();
        source = dao.getSource();

        StringBuilder sb = new StringBuilder();
        JSONArray cats = dao.getCategories();
        int i=0;
        try {
            for (i=0; i<cats.length(); i++) {
                if (i>0) {
                    sb.append(" ");
                }
                sb.append(cats.getString(i));
            }
        } catch (JSONException e) {
            //TODO:  do something more here? ...like truncate the array?...
            e.printStackTrace();
        }
        categories = sb.toString();
    }

    public Photo(FlickrPhoto foto) {
        this();

        url = foto.getUrl();
        //width = foto.getWidth();
        //height = foto.getHeight();
        //type = foto.getType();
        //colordepth = foto.getColorDepth();
        longitude = foto.getLongitude();
        latitude = foto.getLatitude();
        //likes = foto.getLikes();
        //dislikes = foto.getDislikes();
        comment = foto.getTitle();
        // do not expose Flickr usernames...
        //////createdby = foto.getOwner();
        createdby = foto.getOwnername();
        source = PHOTO_SOURCE_FLICKR;
        try {
            createdtime = Long.parseLong(foto.getDateupload());
        } catch (NumberFormatException e) {
            // do nothing, but dont fail; createdtime will be 0...
        }

        StringBuilder allTags = new StringBuilder();
        if (foto.isFamily()) {
            addTag(FLICKR_CATEGORY_FAMILY, allTags);
        }
        if (foto.isFriend()) {
            addTag(FLICKR_CATEGORY_FRIEND, allTags);
        }
        if (foto.isPublic()) {
            addTag(FLICKR_CATEGORY_PUBLIC, allTags);
        }
        // Add whatever tags we have into categories, too...
        String tags = foto.getTags();
        if (tags!=null && tags.length()>1) {
            String[] tagarr = tags.split(" ");
            for (String tag: tagarr) {
                if (tag.length()>1) {
                    addTag(tag, allTags);
                }
            }
        }
        categories = allTags.toString();
    }

    private void addTag(String tag, StringBuilder tags) {
        if (tags.length()>0) {
            tags.append(" ");
        }
        tags.append(tag);
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getColordepth() {
        return colordepth;
    }

    public void setColordepth(int colordepth) {
        this.colordepth = colordepth;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public long getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(long createdtime) {
        this.createdtime = createdtime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
