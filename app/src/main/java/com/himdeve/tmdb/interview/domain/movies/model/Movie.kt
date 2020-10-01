package com.himdeve.tmdb.interview.domain.movies.model

import java.util.*

/**
 * Created by Himdeve on 9/26/2020.
 */
data class Movie(
    val id: Int,
    val title: String,
    val language: String,
    val genres: List<String>,
    val overview: String,
    val releaseDate: String,
    val cover: String,

    val startDate: Calendar?,
    val endDate: Calendar?
)