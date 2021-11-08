package by.alexandr7035.gitstat.extensions

import timber.log.Timber

fun Timber.Forest.debug(message: String) {
    Timber.tag("DEBUG_TAG").d(message)
}