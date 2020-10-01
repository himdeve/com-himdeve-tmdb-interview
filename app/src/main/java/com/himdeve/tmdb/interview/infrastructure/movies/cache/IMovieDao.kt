package com.himdeve.tmdb.interview.infrastructure.movies.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.himdeve.tmdb.interview.infrastructure.movies.cache.model.MovieCacheEntity
import java.util.*

/**
 * Created by Himdeve on 9/26/2020.
 */

@Dao
interface IMovieDao {
    @Query(
        "SELECT * FROM movies" +
                " WHERE (:startDate IS NULL OR startDate = :startDate)" +
                " AND (:endDate IS NULL OR endDate = :endDate)"
    )
    suspend fun getMovies(startDate: Calendar?, endDate: Calendar?): List<MovieCacheEntity>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovie(movieId: Int): MovieCacheEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMovies(movies: List<MovieCacheEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMovie(movie: MovieCacheEntity)
}