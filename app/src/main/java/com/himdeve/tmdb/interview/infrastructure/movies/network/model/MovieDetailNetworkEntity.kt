package com.himdeve.tmdb.interview.infrastructure.movies.network.model

import com.himdeve.tmdb.interview.infrastructure.core.NullToEmptyString
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Himdeve on 9/26/2020.
 */
@JsonClass(generateAdapter = true)
data class MovieDetailNetworkEntity(
    @Json(name = "title")
    val title: String,

    @Json(name = "original_language")
    val language: String,

    @Json(name = "genres")
    val genres: List<GenreNetworkEntity>,

    @Json(name = "overview")
    val overview: String,

    @Json(name = "release_date")
    val releaseDate: String,

    @Json(name = "poster_path")
    @NullToEmptyString
    val cover: String
)