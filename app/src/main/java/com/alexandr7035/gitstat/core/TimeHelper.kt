package com.alexandr7035.gitstat.core

import java.text.SimpleDateFormat
import java.util.*

class TimeHelper {

    fun getDatesRangeForYear_iso8601(year: Int): Iso8601Year {
        return Iso8601Year(
            startDate = "$year-01-01T00:00:00Z",
            endDate = "$year-12-31T23:59:59Z")
    }

    fun getUnixDateFromISO8601(iso8601Date: String): Long {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        format.timeZone = TimeZone.getTimeZone("GMT")
        return format.parse(iso8601Date)!!.time
    }

    fun getYearFromUnixDate(timestamp: Long): Int {
        val format = SimpleDateFormat("yyyy", Locale.US)
        format.timeZone = TimeZone.getTimeZone("GMT")

        return format.format(timestamp).toInt()
    }

    fun getFullFromUnixDate(timestamp: Long): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        return format.format(timestamp)
    }

    data class Iso8601Year(val startDate: String, val endDate: String)
}