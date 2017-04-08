package com.codepath.photospot.services;

import com.codepath.photospot.models.Photo;

import java.util.HashMap;
import java.util.List;

/**
 * End points:
 *
 * Get pix filtered by:
 * - url
 * - location longitude \
 * - location latitude   >  All 3 must be requested together
 * - location radius    /
 * - type
 * - minPixels, maxPixels
 * - minColors, maxColors
 * - category
 * - minLikes maxLikes
 * - minDislikes maxDislikes
 * - createdBy
 * - source
 * - minTime, maxTime
 * - maxResults
 * - skipCount
 * - orderBy:
 *   - Likes (Likes-Dislikes)
 *   - Time  (created_time)
 *
 * Post pic:
 * - Photo object
 *
 * Update pic:
 * - Photo object
 *
 * Delete pic:
 * - Photo object
 *
 * Like a pic:
 * - Photo url
 *
 * Dislike a pic:
 * - Photo url
 *
 * @author dfriedman
 *
 */
public interface ParseService {

    public ServiceResponse<List<Photo>> getPhotos(HashMap<String, Object> params);

    public ServiceResponse<Photo> postPhoto(Photo photo);

    public ServiceResponse<Photo> updatePhoto(Photo photo);

    public ServiceResponse<Photo> deletePhoto(Photo photo);

    public ServiceResponse<Integer> likePhoto(String url);

    public ServiceResponse<Integer> dislikePhoto(String url);
    
}
