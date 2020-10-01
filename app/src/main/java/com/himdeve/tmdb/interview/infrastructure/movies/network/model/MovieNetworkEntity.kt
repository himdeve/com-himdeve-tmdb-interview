package com.himdeve.tmdb.interview.infrastructure.movies.network.model

import com.himdeve.tmdb.interview.infrastructure.core.NullToFalseBoolean
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Himdeve on 9/26/2020.
 */
@JsonClass(generateAdapter = true)
data class MovieNetworkEntity(
    @Json(name = "id")
    val id: Int,

    @Json(name = "adult")
// TODO: Not working: @NullToFalseBoolean
    val adult: Boolean?
)