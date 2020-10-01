package com.himdeve.tmdb.interview.domain.core

/**
 * Created by Himdeve on 9/30/2020.
 */

sealed class ValueOrEmptyDataState<out R> {
    data class ValueDataState<out T>(val data: T) : ValueOrEmptyDataState<T>()
    data class EmptyDataState<out T>(val emptyData: T) : ValueOrEmptyDataState<T>()
}