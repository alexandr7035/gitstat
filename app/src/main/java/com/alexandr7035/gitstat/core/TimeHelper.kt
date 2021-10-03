package com.alexandr7035.gitstat.core

class TimeHelper {

    fun getDatesRangeForYear_iso8601(year: Int): Iso8601Year {
        return Iso8601Year(
            startDate = "$year-01-01T00:00:00Z",
            endDate = "$year-12-31T23:59:59Z")
    }

    data class Iso8601Year(val startDate: String, val endDate: String)
}