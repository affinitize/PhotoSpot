package com.codepath.photospot.daos;

/**
 * Created by pauljmin on 4/8/17.
 */

public class FlickrPhoto {
    static final String photoURL = "https://farm%s.staticflickr.com/%s/%s_%s.jpg";

    String id;
    String farm;
    String server;
    String secret;
    String owner;
    String title;
//    String description;
    String ownername;
    String dateupload;
    String url_m;
    double latitude;
    double longitude;
    String tags;

    boolean isPublic;
    boolean isFriend;
    boolean isFamily;

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
}
