package com.codepath.photospot.daos;

import java.util.List;

/**
 * Created by pauljmin on 4/8/17.
 *
 * This class is needed because of the way the flickr api response is structured and the way Gson works.
 * Need to have an class defined for each layer...
 */

public class FlickrPhotos {
    public Integer page;
    public Integer pages;
    public Integer perpage;
    public Integer total;
    public List<FlickrPhoto> photo = null;
}
