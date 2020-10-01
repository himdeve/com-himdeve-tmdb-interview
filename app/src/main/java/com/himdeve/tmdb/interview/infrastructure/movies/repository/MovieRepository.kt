package com.himdeve.tmdb.interview.infrastructure.movies.repository

import com.himdeve.tmdb.interview.domain.core.DataState
import com.himdeve.tmdb.interview.domain.core.ValueOrEmptyDataState
import com.himdeve.tmdb.interview.domain.movies.model.Movie
import com.himdeve.tmdb.interview.domain.movies.repository.IMovieRepository
import com.himdeve.tmdb.interview.domain.util.DateTimeUtil.toDateString
import com.himdeve.tmdb.interview.domain.util.EntityMapper
import com.himdeve.tmdb.interview.infrastructure.core.BaseRepository
import com.himdeve.tmdb.interview.infrastructure.movies.cache.IMovieCacheDataSource
import com.himdeve.tmdb.interview.infrastructure.movies.cache.model.MovieCacheEntity
import com.himdeve.tmdb.interview.infrastructure.movies.network.IMovieNetworkDataSource
import com.himdeve.tmdb.interview.infrastructure.movies.network.mappers.MovieNetworkMapper
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.util.*

/**
 * Created by Himdeve on 9/26/2020.
 */
class MovieRepository(
    private val cacheDataSource: IMovieCacheDataSource,
    private val networkDataSource: IMovieNetworkDataSource,
    private val cacheMapper: EntityMapper<MovieCacheEntity, Movie>,
    private val networkMapper: MovieNetworkMapper
) : BaseRepository(), IMovieRepository {
    override suspend fun getMovies(
        apiKey: String,
        startDate: Calendar?,
        endDate: Calendar?,
        page: Int,
        pageSize: Int
    ): Flow<DataState<List<Movie>>> = execute(
        databaseQuery = {
            executeDatabaseQuery(startDate, endDate)
        },
        networkCall = {
            executeNetworkCall(apiKey, startDate, endDate, page, pageSize)
        },
        saveCallResult = { movies ->
            executeSaveCallResult(movies)
        }
    )

    // It takes movie detail only from local database
    override fun getMovie(movieId: Int): Flow<DataState<Movie>> = executeOnlyLocally(
        databaseQuery = {
            cacheDataSource.getMovie(movieId).run {
                cacheMapper.mapFromEntity(this)
            }
        }
    )

    private suspend fun executeDatabaseQuery(
        startDate: Calendar?,
        endDate: Calendar?
    ): ValueOrEmptyDataState<List<Movie>> {
        return cacheDataSource.getMovies(startDate, endDate).map { movieCacheEntity ->
            cacheMapper.mapFromEntity(movieCacheEntity)
        }.let { movies ->
            if (movies.isEmpty()) {
                ValueOrEmptyDataState.EmptyDataState(emptyList())
            } else {
                ValueOrEmptyDataState.ValueDataState(movies)
            }
        }
    }

    private suspend fun executeNetworkCall(
        apiKey: String,
        startDate: Calendar?,
        endDate: Calendar?,
        page: Int,
        pageSize: Int
    ): DataState<List<Movie>> {
        val movieChangesEntityRes = networkDataSource.getMovies(
            apiKey,
            startDate?.toDateString(),
            endDate?.toDateString(),
            page
        )

        // return error
        if (movieChangesEntityRes.status == DataState.Status.ERROR) {
            return DataState.error(movieChangesEntityRes.message.orEmpty())
        }

        // TODO: Temporary! TMDb paging not working for changes movies. Then use paging mechanism.
        val temp = movieChangesEntityRes.data?.movieNetworkEntityList?.subList(0, pageSize)

        val movies = temp
            // filter adult movies out
            ?.filter { movieNetworkEntity ->
                movieNetworkEntity.adult != null && !movieNetworkEntity.adult
            }
            // fetch movie details and map it to the domain model; Remove nulls
            ?.mapNotNull { movieNetworkEntity ->
                val movieDetail =
                    networkDataSource.getMovieDetail(apiKey, movieNetworkEntity.id)

                when {
                    movieDetail.status != DataState.Status.SUCCESS -> {
                        // TODO: consider to return an error for the whole function
                        //  -> return@execute movieDetail
                        Timber.e(movieDetail.message)
                        null
                    }
                    movieDetail.data == null -> {
                        null
                    }
                    else -> {
                        networkMapper.mapFromEntity(
                            movieDetail.data,
                            movieNetworkEntity.id,
                            startDate,
                            endDate
                        )
                    }
                }
            }

        return DataState.success(movies.orEmpty())
    }

    private suspend fun executeSaveCallResult(movies: List<Movie>) {
        cacheDataSource.upsertMovies(movies.map { movie ->
            cacheMapper.mapToEntity(movie)
        })
    }
}