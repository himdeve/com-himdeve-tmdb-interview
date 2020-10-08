package com.himdeve.tmdb.interview.domain.movies.repository

import com.himdeve.tmdb.interview.BuildConfig
import com.himdeve.tmdb.interview.domain.core.DataState
import com.himdeve.tmdb.interview.domain.movies.model.Movie
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * Created by Himdeve on 9/26/2020.
 */
interface IMovieRepository {
    fun getMovies(
        apiKey: String = BuildConfig.API_KEY,
        startDate: Calendar? = null,
        endDate: Calendar? = null,
        page: Int = 1,
        pageSize: Int = 50
    ): Flow<DataState<List<Movie>>>

    // Just for local purposes
    fun getMovie(
        movieId: Int
    ): Flow<DataState<Movie>>
}