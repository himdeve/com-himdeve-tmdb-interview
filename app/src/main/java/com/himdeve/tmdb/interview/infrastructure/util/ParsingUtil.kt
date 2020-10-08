package com.himdeve.tmdb.interview.infrastructure.util

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import dagger.Provides
import retrofit2.Response
import timber.log.Timber

/**
 * Created by Himdeve on 10/8/2020.
 */
object ParsingUtil {
    @Provides
    inline fun <reified T> Response<*>.parseErrJsonResponse(): T? {
        // TODO: use Hilt
        val moshi = Moshi.Builder().build()
        val parser = moshi.adapter(T::class.java)
        val response = errorBody()?.string()
        if (response != null)
            try {
                return parser.fromJson(response)
            } catch (e: JsonDataException) {
                Timber.e(e)
            }
        return null
    }
}