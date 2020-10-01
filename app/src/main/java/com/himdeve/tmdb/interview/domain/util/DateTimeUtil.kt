package com.himdeve.tmdb.interview.domain.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Himdeve on 9/30/2020.
 */
object DateTimeUtil {
    private val DATE_FORMAT = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

    // Extension function to Calendar
    fun Calendar.toDateString(): String {
        return DATE_FORMAT.format(time)
    }

    fun String.toCalendar(): Calendar? {
        return DATE_FORMAT.parse(this)?.toCalendar()
    }

    fun Date.toCalendar(): Calendar {
        return Calendar.getInstance().also {
            it.time = this
        }
    }
}