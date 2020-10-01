package com.himdeve.tmdb.interview.infrastructure.movies.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Himdeve on 9/28/2020.
 */
@JsonClass(generateAdapter = true)
class MovieChangesNetworkEntity(
    @Json(name = "results")
    val movieNetworkEntityList: List<MovieNetworkEntity>
)