package by.alexandr7035.gitstat.core.extensions

import android.content.Context
import android.widget.Toast

fun Context.showToast(
    message: String,
    duration: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(this, message, duration).show()
}