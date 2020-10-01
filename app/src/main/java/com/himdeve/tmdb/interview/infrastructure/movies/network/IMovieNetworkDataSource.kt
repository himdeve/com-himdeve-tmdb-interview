package com.himdeve.tmdb.interview.infrastructure.movies.network

import com.himdeve.tmdb.interview.domain.core.DataState
import com.himdeve.tmdb.interview.infrastructure.movies.network.model.MovieChangesNetworkEntity
import com.himdeve.tmdb.interview.infrastructure.movies.network.model.MovieDetailNetworkEntity

/**
 * Created by Himdeve on 9/26/2020.
 */
interface IMovieNetworkDataSource {
    suspend fun getMovies(
        apiKey: String,
        startDate: String?,
        endDate: String?,
        page: Int
    ): DataState<MovieChangesNetworkEntity>

    suspend fun getMovieDetail(
        apiKey: String,
        movieId: Int
    ): DataState<MovieDetailNetworkEntity>
}