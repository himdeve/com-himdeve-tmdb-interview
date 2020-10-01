package com.himdeve.tmdb.interview.infrastructure.core

import com.squareup.moshi.*
import javax.inject.Inject

/**
 * Created by Himdeve on 9/28/2020.
 */

// TODO: NOT WORKING

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class NullToFalseBoolean

class NullToFalseBooleanAdapter @Inject constructor() {
    @ToJson
    fun toJson(@NullToFalseBoolean value: Boolean?): Boolean? {
        return value
    }

    @FromJson
    @NullToFalseBoolean
    fun fromJson(reader: JsonReader): Boolean? {
        val result = when (reader.peek()) {
            JsonReader.Token.STRING -> stringToBoolean(reader.nextString())
            JsonReader.Token.NUMBER -> intToBoolean(reader.nextInt())
            JsonReader.Token.BOOLEAN -> reader.nextBoolean()
            JsonReader.Token.NULL -> reader.nextNull()
            else -> throw JsonDataException()
        }

        return result ?: false
    }

    private fun stringToBoolean(stringFromJson: String): Boolean? {
        return when (stringFromJson.toLowerCase()) {
            "true" -> true
            "false" -> false
            else -> throw JsonDataException()
        }
    }

    private fun intToBoolean(intFromJson: Int): Boolean? {
        return when (intFromJson) {
            1 -> true
            0 -> false
            else -> throw JsonDataException()
        }
    }
}