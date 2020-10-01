package com.himdeve.tmdb.interview.di

import android.content.Context
import androidx.room.Room
import com.himdeve.tmdb.interview.domain.util.Constants
import com.himdeve.tmdb.interview.infrastructure.core.AppDatabase
import com.himdeve.tmdb.interview.infrastructure.movies.cache.IMovieCacheDataSource
import com.himdeve.tmdb.interview.infrastructure.movies.cache.IMovieDao
import com.himdeve.tmdb.interview.infrastructure.movies.cache.MovieCacheDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

/**
 * Created by Himdeve on 9/29/2020.
 */
@Module
@InstallIn(ApplicationComponent::class)
object CacheModule {
    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(
                context,
                AppDatabase::class.java,
                Constants.DATABASE_NAME
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(database: AppDatabase): IMovieDao {
        return database.movieDao()
    }

    @Singleton
    @Provides
    fun provideMovieCacheDataSource(movieDao: IMovieDao): IMovieCacheDataSource {
        return MovieCacheDataSource(movieDao)
    }
}