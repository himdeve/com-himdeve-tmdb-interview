package com.himdeve.tmdb.interview.infrastructure.core

import com.himdeve.tmdb.interview.domain.core.DataState
import com.himdeve.tmdb.interview.infrastructure.core.network.model.ResponseErrMsg
import com.himdeve.tmdb.interview.infrastructure.util.ParsingUtil.parseErrJsonResponse
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
                if (body != null) return DataState.Success(body)
            }

            var errorMsg = response.message()
            if (errorMsg.isEmpty()) {
                errorMsg = response.parseErrJsonResponse<ResponseErrMsg>()?.errorMessage.orEmpty()
            }

            // TODO: consider to take an error code from error body if response.message() is empty
            return error("${response.code()}: $errorMsg")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): DataState<T> {
        Timber.d(message)
        return DataState.Error(message)
    }
}