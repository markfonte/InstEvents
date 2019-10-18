# InstEvents

InstEvents is the quickest and most seamless way to view current and upcoming events in you area. Download our android application today to browse events on our embedded Google Map or view more details in our Browse section. Add an event yourself with our smooth interface featuring date/time pickers, location autocomplete and cover photo uploads. No login required. Stay tuned for more updates!

<img src="https://firebasestorage.googleapis.com/v0/b/infinite-chain-255705.appspot.com/o/example_screenshots%2Fexample_01.jpg?alt=media&token=81bdb20f-b71b-4d5c-b3b6-632e5a28a5a4" alt="Browse Events" width="280" > <img src="https://firebasestorage.googleapis.com/v0/b/infinite-chain-255705.appspot.com/o/example_screenshots%2Fexample_02.jpg?alt=media&token=55bd7513-966c-467b-bd07-63d5a9b634d0" alt="Add Event Time Picker" width="280" > <img src="https://firebasestorage.googleapis.com/v0/b/infinite-chain-255705.appspot.com/o/example_screenshots%2Fexample_03.jpg?alt=media&token=fa203ce9-9d53-4afe-8f92-f961d49b61e2" alt="Google Places API" width="280" > <img src="https://firebasestorage.googleapis.com/v0/b/infinite-chain-255705.appspot.com/o/example_screenshots%2Fexample_04.jpg?alt=media&token=50996ab1-eaa0-40c9-bb07-3fede8498159" alt="Add Event" width="280" > <img src="https://firebasestorage.googleapis.com/v0/b/infinite-chain-255705.appspot.com/o/example_screenshots%2Fexample_05.jpg?alt=media&token=1d2d0c32-f7e3-47e9-84ef-8bc7aea538b4" alt="Embedded Map View" width="280" >

## Built With

* [Google Maps SDK For Android](https://developers.google.com/maps/documentation/android-sdk/intro) - Embedded map view
* [Google Maps Places API](https://developers.google.com/places/web-service/intro) - Location autocomplete when adding events
* [Google Maps Geocoding API](https://developers.google.com/maps/documentation/geocoding/start) - Converts plain English locations into latitude/longitude coordinates
* [Google Cloud Platform](https://console.cloud.google.com/) - Console for configuring all of our APIs
* [Firebase Storage](https://firebase.google.com/docs/storage/) - Holds cover photos of events
* [Firebase Cloud Functions](https://firebase.google.com/docs/functions/) - Hosts web server that filters events to send to application
* [Firebase Firestore](https://firebase.google.com/docs/firestore/) - NoSQL storage of event information
* [Android Volley](https://github.com/google/volley) - Sends HTTP requests to server
* [BumpTech Glide](https://github.com/bumptech/glide) - Loads and caches images in Android application


## Future Plans

* Release to Google Play Store
* Scrape the web for popular public events in your area
* Tabs to change days on embedded map view
* More robust event info window on embedded map view
* And more!

## Installation / Development Set Up

    git clone https://github.com/markfonte/InstEvents/
    cd InstEvents

### Android

First, copy over the sample api keys file to the real file name.

    cp android/app/src/main/res/values/SAMPLE_google_maps_api.xml android/app/src/main/res/values/google_maps_api.xml

Fill out the google_maps_api.xml with keys from your own GCP account (instructions for this coming soon). Each of the keys need access to the respective service.

Open the "InstEvents/android" folder in Android Studio. Then you can build the app, and install it on your phone or run it in an emulator.

### Backend (Cloud Functions)

(How to deploy to your own GCP account.)

Coming soon.

## Contributing

Fork the repository, and follow the installation guide above.

Make improvements, etc. and then create a pull request on this repo's master branch with your proposed changes. We'll discuss and review the code, give feedback, etc. Then, if we approve the changes and Travis CI passes the pull request, we'll merge it!

More detailed instructions coming soon.

## Authors

* **Nathan Johnson** - *Full stack developer* - [nathan815](https://github.com/nathan815)
* **Mark Fonte** - *Full stack developer* - [markfonte](https://github.com/markfonte)

See also the list of [contributors](https://github.com/markfonte/InstEvents/contributors) who participated in this project.

## Acknowledgments

* [MHacks 12](https://mhacks.org), where we began this project! Thank you!
