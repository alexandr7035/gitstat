package by.alexandr7035.gitstat.core

import by.alexandr7035.gitstat.core.extensions.getStringDateFromLong
import by.alexandr7035.gitstat.core.extensions.getUnixDateFromStringFormat
import java.text.SimpleDateFormat
import java.util.*

class TimeHelper {

    fun getDatesRangeForYear_iso8601(year: Int): Iso8601Year {
        return Iso8601Year(
            startDate = "$year-01-01T00:00:00Z",
            endDate = "$year-12-31T23:59:59Z")
    }

    fun getUnixDateFromISO8601(iso8601Date: String): Long {
        return iso8601Date.getUnixDateFromStringFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", "GMT")
    }

    // Example input date: "2016-01-01"
    // Used in github contributions
    fun getUnixDateFrom_yyyyMMdd(stringDate: String): Long {
        return stringDate.getUnixDateFromStringFormat("yyyy-MM-dd", "GMT")
    }

    fun getYearFromUnixDate(timestamp: Long): Int {
        return timestamp.getStringDateFromLong("yyyy", "GMT").toInt()
    }

    fun getFullFromUnixDate(timestamp: Long): String {
        return timestamp.getStringDateFromLong("yyyy-MM-dd HH:mm")
    }

    fun getBeginningOfDayForUnixDate(currentDate: Long): Long {
        val beginningStr = currentDate.getStringDateFromLong("yyyy-MM-dd", "GMT")
        return getUnixDateFrom_yyyyMMdd(beginningStr)
    }

    fun getBeginningOfDayForUnixDate_currentTz(currentDate: Long): Long {
        val beginningStr = currentDate.getStringDateFromLong("yyyy-MM-dd")
        return getUnixDateFrom_yyyyMMdd(beginningStr)
    }

    // TODO remove (duplicate)
    fun getCurrentYearForUnixDate(currentDate: Long): Int {
        val format = SimpleDateFormat("yyyy", Locale.US)
        format.timeZone = TimeZone.getTimeZone("GMT")
        val yearStr = format.format(currentDate)

        return yearStr.toInt()
    }

    // Use as month ID in database
    fun get_yyyyMM_fromUnixDate(unixDate: Long): String {
        return unixDate.getStringDateFromLong("yyyyMM", "GMT")
    }

    data class Iso8601Year(val startDate: String, val endDate: String)
}