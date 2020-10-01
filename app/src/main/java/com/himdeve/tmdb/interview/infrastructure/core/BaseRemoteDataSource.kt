package com.himdeve.tmdb.interview.infrastructure.core

import com.himdeve.tmdb.interview.domain.core.DataState
import retrofit2.Response
import timber.log.Timber

/**
 * Created by Himdeve on 9/26/2020.
 */
abstract class BaseRemoteDataSource {
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): DataState<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return DataState.success(body)
            }

            // TODO: if message is empty, take error from response's error body!
            return error("${response.code()}: ${response.message()}")
        } catch (e: Exception) {
            e.printStackTrace()
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): DataState<T> {
        Timber.d(message)
        return DataState.error(message)
    }
}