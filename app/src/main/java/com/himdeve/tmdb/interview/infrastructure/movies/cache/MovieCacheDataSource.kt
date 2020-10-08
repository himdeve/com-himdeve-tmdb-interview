package com.himdeve.tmdb.interview.infrastructure.movies.cache

import com.himdeve.tmdb.interview.infrastructure.movies.cache.model.MovieCacheEntity
import java.util.*

/**
 * Created by Himdeve on 9/26/2020.
 */
class MovieCacheDataSource(
    private val movieDao: IMovieDao
) : IMovieCacheDataSource {
    override suspend fun getMovies(
        startDate: Calendar?,
        endDate: Calendar?
    ): List<MovieCacheEntity> {
        return movieDao.getMovies(startDate, endDate)
    }

    override suspend fun getMovie(movieId: Int): MovieCacheEntity {
        return movieDao.getMovie(movieId)
    }

    override suspend fun upsertMovies(movies: List<MovieCacheEntity>) {
        movieDao.upsertMovies(movies)
    }

    override suspend fun upsertMovie(movie: MovieCacheEntity) {
        movieDao.upsertMovie(movie)
    }

    override suspend fun clear() {
        movieDao.clearMovies()
    }
}