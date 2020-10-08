package com.himdeve.tmdb.interview.infrastructure.core.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Himdeve on 10/8/2020.
 */
@JsonClass(generateAdapter = true)
class ResponseErrMsg(
    @Json(name = "status_code")
    val errorCode: String,

    @Json(name = "status_message")
    val errorMessage: String
)