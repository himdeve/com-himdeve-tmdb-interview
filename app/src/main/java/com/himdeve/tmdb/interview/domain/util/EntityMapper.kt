package com.himdeve.tmdb.interview.domain.util

/**
 * Created by Himdeve on 9/26/2020.
 */
interface EntityMapper<Entity, DomainModel> {

    fun mapFromEntity(entity: Entity): DomainModel

    fun mapToEntity(domainModel: DomainModel): Entity
}