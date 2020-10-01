package com.himdeve.tmdb.interview.infrastructure.movies.network.mappers

import com.himdeve.tmdb.interview.domain.movies.model.Movie
import com.himdeve.tmdb.interview.infrastructure.movies.network.model.MovieDetailNetworkEntity
import java.util.*
import javax.inject.Inject

/**
 * Created by Himdeve on 9/26/2020.
 */
class MovieNetworkMapper @Inject constructor() {
    fun mapFromEntity(
        entity: MovieDetailNetworkEntity,
        movieId: Int,
        startDate: Calendar?,
        endDate: Calendar?
    ): Movie {
        return Movie(
            id = movieId,
            title = entity.title,
            language = entity.language,
            genres = entity.genres.map { genreNetworkEntity ->
                genreNetworkEntity.name
            },
            overview = entity.overview,
            releaseDate = entity.releaseDate,
            cover = entity.cover,
            startDate = startDate,
            endDate = endDate
        )
    }
}