package com.himdeve.tmdb.interview.infrastructure.movies.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Himdeve on 9/26/2020.
 */
@JsonClass(generateAdapter = true)
data class GenreNetworkEntity(
    @Json(name = "id")
    val id: Int,

    @Json(name = "name")
    val name: String
)