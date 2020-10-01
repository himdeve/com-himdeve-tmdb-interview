package com.himdeve.tmdb.interview.infrastructure.core

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.himdeve.tmdb.interview.infrastructure.movies.cache.IMovieDao
import com.himdeve.tmdb.interview.infrastructure.movies.cache.model.MovieCacheEntity

/**
 * Created by Himdeve on 9/26/2020.
 */
@Database(entities = [MovieCacheEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): IMovieDao
}