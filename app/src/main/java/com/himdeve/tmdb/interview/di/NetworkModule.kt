package com.himdeve.tmdb.interview.di

import com.himdeve.tmdb.interview.domain.util.Constants
import com.himdeve.tmdb.interview.infrastructure.core.HttpRequestInterceptor
import com.himdeve.tmdb.interview.infrastructure.core.NullToEmptyStringAdapter
import com.himdeve.tmdb.interview.infrastructure.core.NullToFalseBooleanAdapter
import com.himdeve.tmdb.interview.infrastructure.movies.network.IMovieNetworkDataSource
import com.himdeve.tmdb.interview.infrastructure.movies.network.IMovieNetworkService
import com.himdeve.tmdb.interview.infrastructure.movies.network.MovieNetworkDataSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Himdeve on 9/29/2020.
 */
@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideMoshi(
        nullToEmptyStringAdapter: NullToEmptyStringAdapter,
        nullToFalseBooleanAdapter: NullToFalseBooleanAdapter
    ): Moshi {
        return Moshi.Builder()
            .add(nullToEmptyStringAdapter)
            .add(nullToFalseBooleanAdapter)
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideHttpRequestInterceptor(): Interceptor {
        return HttpRequestInterceptor()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(httpRequestInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpRequestInterceptor)
            .connectTimeout(Constants.CONNECTION_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
            .readTimeout(Constants.CONNECTION_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.TMDB_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieNetworkService(retrofit: Retrofit): IMovieNetworkService {
        return retrofit.create(IMovieNetworkService::class.java)
    }

    @Singleton
    @Provides
    fun provideMovieNetworkDataSource(
        movieNetworkService: IMovieNetworkService
    ): IMovieNetworkDataSource {
        return MovieNetworkDataSource(movieNetworkService)
    }
}