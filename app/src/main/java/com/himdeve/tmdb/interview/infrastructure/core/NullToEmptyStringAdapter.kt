package com.himdeve.tmdb.interview.infrastructure.core

import androidx.annotation.Nullable
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.JsonReader
import com.squareup.moshi.ToJson
import javax.inject.Inject

/**
 * Created by Himdeve on 9/28/2020.
 */
@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class NullToEmptyString

class NullToEmptyStringAdapter @Inject constructor() {
    @ToJson
    fun toJson(@NullToEmptyString value: String?): String? {
        return value
    }

    @FromJson
    @NullToEmptyString
    fun fromJson(reader: JsonReader): String? {
        val result = if (reader.peek() === JsonReader.Token.NULL) {
            reader.nextNull()
        } else {
            reader.nextString()
        }

        return result ?: ""
    }
}