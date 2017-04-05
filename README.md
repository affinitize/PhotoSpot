# PhotoSpot

Photo Location app.  ShotSpot is an app that can be used to look for places to take photos.  Whether it be while you're on vacation, or you're at home, if you want take some photos, this app will tell you where to go to take the photos you want.  You search based on location and type of photos you want to take and the app with returns places that match your filters.


## User Stories
* [ ] User can search app for locations that are good for taking pictures by location and photo type
* [ ] User can filter results based on the type of photo they want to take (landscape, architecture, etc)
* [ ] Search results can be viewed in a list or in a map view with pins for each result
* [ ] User can click a location (in list or map view) to get a detail view of the location that includes
  * [ ] Pictures that have been taken there (from flickr)
  * [ ] Pictures that other users have taken at this location and uploaded
  * [ ] General details about location 
     * [ ] include notes, comments for location

## Stretch goals
* [ ] Users can rate pictures and the app uses that to determine which pictures are shown for each location
* [ ] User profile view which shows all a user's photos organized by location
* [ ] Add a section for photos posted by experienced users that also include tips on how to achieve the photos shown.
* [ ] Add multiple tags for photos
* [ ] share photos with friends
* [ ] User can upload photos that they take and map them to location
  * [ ] User can "publish" photos that they upload which would they allow other users to see their photos

## Apis we will be using for our primary stories
* Google places
* Google location
* Parse
* Flickr

## Parse schema
### photos
```json
{
  "http://my.site.com/mypics/1234.gif": {
    "width": 640,
    "height": 480,
    "type": "GIF",
    "colordepth": 256,
    "longitude": 157.123,
    "latitude": -112.512,
    "category": "building",
    "likes": 1021,
    "dislikes": 1,
    "notes": "this is a note",
    "timeTaken": "312443542", 
    }
 }
```

