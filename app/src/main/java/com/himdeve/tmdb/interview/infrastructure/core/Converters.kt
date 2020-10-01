package com.himdeve.tmdb.interview.infrastructure.core

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

import androidx.room.TypeConverter
import com.himdeve.tmdb.interview.domain.util.DateTimeUtil.toCalendar
import com.himdeve.tmdb.interview.domain.util.DateTimeUtil.toDateString
import java.util.*


/**
 * Created by Himdeve on 9/26/2020.
 */

/**
 * Type converters to allow Room to reference complex data types.
 */
class Converters {
    private val moshi = Moshi.Builder().build()
    private val membersType = Types.newParameterizedType(List::class.java, String::class.java)
    private val membersAdapter = moshi.adapter<List<String>>(membersType)

    @TypeConverter
    fun calendarToDateStamp(calendar: Calendar?): String? {
        return calendar?.toDateString()
    }

    @TypeConverter
    fun dateStampToCalendar(value: String?): Calendar? {
        return value?.toCalendar()
    }

    @TypeConverter
    fun jsonStringToList(string: String): List<String> {
        return membersAdapter.fromJson(string).orEmpty()
    }

    @TypeConverter
    fun listToJsonString(members: List<String>): String {
        return membersAdapter.toJson(members)
    }
}