package by.alexandr7035.gitstat.core.extensions

import timber.log.Timber

fun Timber.Forest.debug(message: String) {
    tag("DEBUG_TAG").d(message)
}