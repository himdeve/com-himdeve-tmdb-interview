package com.himdeve.tmdb.interview.infrastructure.movies.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import java.util.*

/**
 * Created by Himdeve on 9/26/2020.
 */

@Entity(tableName = "movies")
@JsonClass(generateAdapter = true)
data class MovieCacheEntity(
    @PrimaryKey @ColumnInfo(name = "id")
    val movieId: Int,

    val title: String,
    val language: String,
    val genres: List<String>,
    val overview: String,
    val releaseDate: String,
    val cover: String,

    val startDate: Calendar?,
    val endDate: Calendar?
)