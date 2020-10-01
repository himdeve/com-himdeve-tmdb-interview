package com.himdeve.tmdb.interview.infrastructure.movies.network

import com.himdeve.tmdb.interview.domain.core.DataState
import com.himdeve.tmdb.interview.infrastructure.core.BaseRemoteDataSource
import com.himdeve.tmdb.interview.infrastructure.movies.network.model.MovieChangesNetworkEntity
import com.himdeve.tmdb.interview.infrastructure.movies.network.model.MovieDetailNetworkEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Himdeve on 9/26/2020.
 */
class MovieNetworkDataSource(
    private val networkService: IMovieNetworkService
) : BaseRemoteDataSource(), IMovieNetworkDataSource {
    override suspend fun getMovies(
        apiKey: String,
        startDate: String?,
        endDate: String?,
        page: Int
    ): DataState<MovieChangesNetworkEntity> = withContext(Dispatchers.IO) {
        getResult {
            networkService.getMovies(apiKey, startDate, endDate, page)
        }
    }

    override suspend fun getMovieDetail(
        apiKey: String,
        movieId: Int
    ): DataState<MovieDetailNetworkEntity> = withContext(Dispatchers.IO) {
        getResult { networkService.getMovieDetail(movieId, apiKey) }
    }
}