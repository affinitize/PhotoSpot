package com.codepath.photospot.daos;

import org.parceler.Parcel;

/**
 * Created by pauljmin on 4/8/17.
 */

@Parcel
public class FlickrPhoto {
    static final String photoURL = "https://farm%s.staticflickr.com/%s/%s_%s.jpg";

    public String id;
    public String farm;
    public String server;
    public String secret;
    public String owner;
    public String title;
//    String description;
public String ownername;
    public String dateupload;
    public String url_m;
    public double latitude;
    double longitude;
    public String tags;

    public boolean isPublic;
    public boolean isFriend;
    public boolean isFamily;

    public FlickrPhoto() {}

    public String getUrl() {
        return String.format(photoURL, farm, server, id, secret);
    }

    public String getId() {
        return id;
    }

    public String getFarm() {
        return farm;
    }

    public String getServer() {
        return server;
    }

    public String getSecret() {
        return secret;
    }

    public String getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public boolean isFamily() {
        return isFamily;
    }


    public static String getPhotoURL() {
        return photoURL;
    }

    public String getOwnername() {
        return ownername;
    }

    public String getDateupload() {
        return dateupload;
    }

    public String getUrl_m() {
        return url_m;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTags() {
        return tags;
    }
}
