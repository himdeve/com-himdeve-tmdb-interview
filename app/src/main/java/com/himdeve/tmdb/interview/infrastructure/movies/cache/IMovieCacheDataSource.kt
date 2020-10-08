package com.himdeve.tmdb.interview.infrastructure.movies.cache

import com.himdeve.tmdb.interview.infrastructure.movies.cache.model.MovieCacheEntity
import java.util.*

/**
 * Created by Himdeve on 9/26/2020.
 */
interface IMovieCacheDataSource {
    suspend fun getMovies(
        startDate: Calendar?,
        endDate: Calendar?,
    ): List<MovieCacheEntity>

    suspend fun getMovie(
        movieId: Int,
    ): MovieCacheEntity

    suspend fun upsertMovies(
        movies: List<MovieCacheEntity>
    )

    suspend fun upsertMovie(
        movie: MovieCacheEntity
    )

    suspend fun clear()
}