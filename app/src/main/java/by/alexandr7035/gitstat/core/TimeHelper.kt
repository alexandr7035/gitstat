package by.alexandr7035.gitstat.core

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

    // Example input date: "2016-01-01"
    // Used in github contributions
    fun getUnixDateFrom_yyyyMMdd(stringDate: String): Long {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        format.timeZone = TimeZone.getTimeZone("GMT")
        return format.parse(stringDate)!!.time
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

    fun getBeginningOfDayForUnixDate(currentDate: Long): Long {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        format.timeZone = TimeZone.getTimeZone("GMT")
        val beginningStr = format.format(currentDate)

        return getUnixDateFrom_yyyyMMdd(beginningStr)
    }

    fun getCurrentYearForUnixDate(currentDate: Long): Int {
        val format = SimpleDateFormat("yyyy", Locale.US)
        format.timeZone = TimeZone.getTimeZone("GMT")
        val yearStr = format.format(currentDate)

        return yearStr.toInt()
    }

    fun get_yyyyMM_fromUnixDate(unixDate: Long): String {
        val format = SimpleDateFormat("yyyyMM", Locale.US)
        format.timeZone = TimeZone.getTimeZone("GMT")
        return format.format(unixDate)
    }

    data class Iso8601Year(val startDate: String, val endDate: String)
}