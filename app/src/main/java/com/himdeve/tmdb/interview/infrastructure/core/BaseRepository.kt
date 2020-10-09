package com.himdeve.tmdb.interview.infrastructure.core

import com.himdeve.tmdb.interview.domain.core.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Created by Himdeve on 9/26/2020.
 */
@ExperimentalCoroutinesApi
abstract class BaseRepository {
    protected fun <T, A> execute(
        databaseQuery: suspend () -> T,
        networkCall: suspend () -> DataState<A>,
        clearDatabaseTable: suspend () -> Unit,
        saveCallResult: suspend (A) -> Unit
    ): Flow<DataState<T>> =
        flow {
            emit(DataState.Loading())

            // emit cached values first
            emit(DataState.Success(databaseQuery.invoke()))

            emit(DataState.Loading())

            val responseStatus = networkCall.invoke()
            if (responseStatus is DataState.Success) {
                // clear database data if desired
                clearDatabaseTable.invoke()

                // cache network data
                saveCallResult(responseStatus.data!!)

                // emit new cached values (DB as single source of truth)
                emit(DataState.Success(databaseQuery.invoke()))
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
}