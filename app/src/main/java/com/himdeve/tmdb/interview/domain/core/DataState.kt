package com.himdeve.tmdb.interview.domain.core

/**
 * Created by Himdeve on 9/26/2020.
 */
sealed class DataState<out T> {
    class Loading<out T> : DataState<T>()
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error<out T>(val message: String) : DataState<T>()
}