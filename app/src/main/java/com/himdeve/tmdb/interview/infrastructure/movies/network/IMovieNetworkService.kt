package com.himdeve.tmdb.interview.infrastructure.movies.network

import com.himdeve.tmdb.interview.infrastructure.movies.network.model.MovieDetailNetworkEntity
import com.himdeve.tmdb.interview.infrastructure.movies.network.model.MovieChangesNetworkEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Himdeve on 9/26/2020.
 */
interface IMovieNetworkService {
    @GET("movie/changes")
    suspend fun getMovies(
        @Query("api_key")
        apiKey: String,

        @Query("start_date")
        start_date: String?,

        @Query("end_date")
        end_date: String?,

        @Query("page")
        page: Int
    ): Response<MovieChangesNetworkEntity>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id")
        movieId: Int,

        @Query("api_key")
        apiKey: String
    ): Response<MovieDetailNetworkEntity>
}