package com.himdeve.tmdb.interview.di

import com.himdeve.tmdb.interview.domain.movies.model.Movie
import com.himdeve.tmdb.interview.domain.movies.repository.IMovieRepository
import com.himdeve.tmdb.interview.domain.util.EntityMapper
import com.himdeve.tmdb.interview.infrastructure.movies.cache.IMovieCacheDataSource
import com.himdeve.tmdb.interview.infrastructure.movies.cache.mapper.MovieCacheMapper
import com.himdeve.tmdb.interview.infrastructure.movies.cache.model.MovieCacheEntity
import com.himdeve.tmdb.interview.infrastructure.movies.network.IMovieNetworkDataSource
import com.himdeve.tmdb.interview.infrastructure.movies.network.mappers.MovieNetworkMapper
import com.himdeve.tmdb.interview.infrastructure.movies.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

/**
 * Created by Himdeve on 9/29/2020.
 */
@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideMovieRepository(
        movieCacheDataSource: IMovieCacheDataSource,
        movieNetworkDataSource: IMovieNetworkDataSource,
        movieCacheMapper: EntityMapper<MovieCacheEntity, Movie>,
        movieNetworkMapper: MovieNetworkMapper
    ): IMovieRepository {
        return MovieRepository(
            movieCacheDataSource,
            movieNetworkDataSource,
            movieCacheMapper,
            movieNetworkMapper
        )
    }

    @Singleton
    @Provides
    fun provideMovieCacheMapper(): EntityMapper<MovieCacheEntity, Movie> {
        return MovieCacheMapper()
    }
}