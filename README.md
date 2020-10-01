# Himdeve TMDb

## About me
Himdeve development
https://himdeve.com

## Info

Android Kotlin application to demonstrate use of TMDb API

## Technologies

Android, Kotlin, Domain-driven-design (DDD), MVVM, Coroutines, Room, Retrofit, Moshi, Hilt, LiveData, Flow

## TODOs

Adding the Paging 3 library 
  (Actually now there is a problem with TMDb API for GET /movie/changes. Paging is currently not working)

## Build
1. In the Android Project create keystore.properties file to set your release information
2. In the Android Project create tmdb.properties file to set API KEY to TMDb API
  - this file should look like this:
  tmdb_api_key=<YOUR_TMDB_API_KEY>  
  - for API KEY you need to register here: https://www.themoviedb.org/signup
  
## TMDb API
List of movie ids: https://developers.themoviedb.org/3/changes/get-movie-change-list

Movie detail for id: https://developers.themoviedb.org/3/movies

## Screenshots

![phone_portrait](https://user-images.githubusercontent.com/16305136/94871794-27b28000-044b-11eb-8f85-c63ff382e7d9.jpg)

![phone_portrait_detail](https://user-images.githubusercontent.com/16305136/94871826-4153c780-044b-11eb-82b8-bbe648c372c6.jpg)

![phone_landscape](https://user-images.githubusercontent.com/16305136/94871849-4c0e5c80-044b-11eb-97fe-1327dc1d5f65.jpg)

![tablet_landscape](https://user-images.githubusercontent.com/16305136/94871863-54669780-044b-11eb-8cf3-81e76ff0de0f.jpg)

![tablet_landscape_detail](https://user-images.githubusercontent.com/16305136/94871881-5e889600-044b-11eb-9d02-ab6fc13a72b5.jpg)
