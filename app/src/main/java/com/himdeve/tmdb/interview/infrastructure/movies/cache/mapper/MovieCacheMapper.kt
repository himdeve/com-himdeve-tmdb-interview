package com.himdeve.tmdb.interview.infrastructure.movies.cache.mapper

import com.himdeve.tmdb.interview.domain.movies.model.Movie
import com.himdeve.tmdb.interview.domain.util.EntityMapper
import com.himdeve.tmdb.interview.infrastructure.movies.cache.model.MovieCacheEntity

/**
 * Created by Himdeve on 9/26/2020.
 */
class MovieCacheMapper : EntityMapper<MovieCacheEntity, Movie> {
    override fun mapFromEntity(entity: MovieCacheEntity): Movie {
        return Movie(
            id = entity.movieId,
            title = entity.title,
            language = entity.language,
            genres = entity.genres,
            overview = entity.overview,
            releaseDate = entity.releaseDate,
            cover = entity.cover,
            startDate = entity.startDate,
            endDate = entity.endDate
        )
    }

    override fun mapToEntity(domainModel: Movie): MovieCacheEntity {
        return MovieCacheEntity(
            movieId = domainModel.id,
            title = domainModel.title,
            language = domainModel.language,
            genres = domainModel.genres,
            overview = domainModel.overview,
            releaseDate = domainModel.releaseDate,
            cover = domainModel.cover,
            startDate = domainModel.startDate,
            endDate = domainModel.endDate
        )
    }
}