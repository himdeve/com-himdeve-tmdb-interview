package com.himdeve.tmdb.interview.infrastructure.core

import com.himdeve.tmdb.interview.domain.core.DataState
import com.himdeve.tmdb.interview.domain.core.ValueOrEmptyDataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

/**
 * Created by Himdeve on 9/26/2020.
 */
abstract class BaseRepository {
    protected fun <T, A> execute(
        databaseQuery: suspend () -> ValueOrEmptyDataState<T>,
        networkCall: suspend () -> DataState<A>,
        saveCallResult: suspend (A) -> Unit
    ): Flow<DataState<T>> =
        flow {
            emit(DataState.loading())

            // emit cached values first if there are some, otherwise keep loading
            emitLocalDataIfAny(databaseQuery)

            val responseStatus = networkCall.invoke()
            if (responseStatus.status == DataState.Status.SUCCESS) {
                // cache network data
                saveCallResult(responseStatus.data!!)

                // emit new cached values (DB as single source of truth)
                emitLocalDataIfAny(databaseQuery, true)
            } else if (responseStatus.status == DataState.Status.ERROR) {
                emit(DataState.error(responseStatus.message!!))
            }
        }

    protected fun <T> executeOnlyLocally(
        databaseQuery: suspend () -> T,
    ): Flow<DataState<T>> =
        flow {
            emit(DataState.loading())

            // emit cached value
            emit(DataState.success(databaseQuery.invoke()))
        }

    private suspend fun <T> FlowCollector<DataState<T>>.emitLocalDataIfAny(
        databaseQuery: suspend () -> ValueOrEmptyDataState<T>,
        emitEmpty: Boolean = false
    ) {
        when (val cachedDataState = databaseQuery.invoke()) {
            is ValueOrEmptyDataState.ValueDataState -> emit(DataState.success(cachedDataState.data))
            is ValueOrEmptyDataState.EmptyDataState -> {
                if (emitEmpty) {
                    emit(DataState.success(cachedDataState.emptyData))
                }
            }
        }
    }
}