package by.alexandr7035.gitstat.core.helpers

enum class TimeString(val value: String) {
    YEAR_ONLY("yyyy"),
    YEARS_MONTHS_DAYS("yyyy-MM-dd"),
    ISO_8601("yyyy-MM-dd'T'HH:mm:ss'Z'")
}