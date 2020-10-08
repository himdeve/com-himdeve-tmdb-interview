package com.himdeve.tmdb.interview.infrastructure.core

import com.himdeve.tmdb.interview.domain.core.DataState
import com.himdeve.tmdb.interview.domain.core.ValueOrEmptyDataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.Dispatcher

/**
 * Created by Himdeve on 9/26/2020.
 */
@ExperimentalCoroutinesApi
abstract class BaseRepository {
    protected fun <T, A> execute(
        databaseQuery: suspend () -> ValueOrEmptyDataState<T>,
        networkCall: suspend () -> DataState<A>,
        saveCallResult: suspend (A) -> Unit
    ): Flow<DataState<T>> =
        flow {
            emit(DataState.Loading())

            // emit cached values first if there are some, otherwise keep loading
            emitLocalDataIfAny(databaseQuery)

            val responseStatus = networkCall.invoke()
            if (responseStatus is DataState.Success) {
                // cache network data
                saveCallResult(responseStatus.data!!)

                // emit new cached values (DB as single source of truth)
                emitLocalDataIfAny(databaseQuery, true)
            } else if (responseStatus is DataState.Error) {
                emit(DataState.Error(responseStatus.message))
            }
        }.flowOn(Dispatchers.IO)

    protected fun <T> executeOnlyLocally(
        databaseQuery: suspend () -> T,
    ): Flow<DataState<T>> =
        flow {
            emit(DataState.Loading())

            // emit cached value
            emit(DataState.Success(databaseQuery.invoke()))
        }.flowOn(Dispatchers.IO)

    private suspend fun <T> FlowCollector<DataState<T>>.emitLocalDataIfAny(
        databaseQuery: suspend () -> ValueOrEmptyDataState<T>,
        emitEmpty: Boolean = false
    ) {
        when (val cachedDataState = databaseQuery.invoke()) {
            is ValueOrEmptyDataState.ValueDataState -> emit(DataState.Success(cachedDataState.data))
            is ValueOrEmptyDataState.EmptyDataState -> {
                if (emitEmpty) {
                    emit(DataState.Success(cachedDataState.emptyData))
                }
            }
        }
    }
}