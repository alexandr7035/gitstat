package by.alexandr7035.gitstat.core.extensions

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import java.text.SimpleDateFormat
import java.util.*

fun String.getClickableSpannable(
    clickableText: String,
    clickListener: View.OnClickListener,
    isBold: Boolean,
    spannableColor: Int? = null,
    isUnderlined: Boolean = true
): SpannableString {

    val startIndex = this.indexOf(clickableText, startIndex = 0, ignoreCase = false)
    val endIndex = startIndex + clickableText.length

    val spannableString = SpannableString(this)

    val clickable = object : ClickableSpan() {
        override fun onClick(view: View) {
            clickListener.onClick(view)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = isUnderlined

            if (spannableColor != null) {
                ds.color = spannableColor
            }
        }
    }

    spannableString.setSpan(clickable, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    if (isBold) {
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    return spannableString
}


fun String.getUnixDateFromStringFormat(
    formatStr: String,
    timeZoneStr: String? = null,
    dateLocale: Locale = Locale.US
): Long {
    val format = SimpleDateFormat(formatStr, dateLocale)

    if (timeZoneStr != null) {
        format.timeZone = TimeZone.getTimeZone(timeZoneStr)
    }
    return format.parse(this)!!.time
}