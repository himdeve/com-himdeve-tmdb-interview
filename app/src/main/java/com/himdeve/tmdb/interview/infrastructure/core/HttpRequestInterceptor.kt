package com.himdeve.tmdb.interview.infrastructure.core

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

/**
 * Created by Himdeve on 9/28/2020.
 */
class HttpRequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder().url(originalRequest.url()).build()
        Timber.d(request.toString())
        return chain.proceed(request)
    }
}