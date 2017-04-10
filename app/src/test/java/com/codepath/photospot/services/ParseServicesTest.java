package com.codepath.photospot.services;

import com.codepath.photospot.PhotoSpotApplication;
import com.codepath.photospot.models.Photo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by dfrie on 4/8/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class ParseServicesTest {

    @Mock
    private PhotoSpotApplication app;

    @Mock
    private ParseService parseService;

    @Test
    public void sanity() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void getParsePhotos() throws Exception {

        HashMap<String, Object> params =  new HashMap<>();
        List<Photo> photos = new ArrayList<Photo>();
        ServiceResponse<List<Photo>> resp = new ServiceResponse();
        resp.set(photos);

        when(parseService.getPhotos(params))
                .thenReturn(resp);

        ServiceResponse<List<Photo>> response = parseService.getPhotos(params);

        List<Photo> list = response.get();

        assertNotNull(list);
        assertTrue(list.size()==0);
    }
}
