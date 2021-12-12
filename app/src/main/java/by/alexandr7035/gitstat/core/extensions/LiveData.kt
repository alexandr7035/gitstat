package by.alexandr7035.gitstat.core.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

// Sometimes we may receive nulls in livedata observers
// For example, when room cache tables are cleared before saving updated data
// So we just skip null updates to avoid null checks
//
// See https://proandroiddev.com/nonnull-livedata-with-kotlin-extension-26963ffd0333
fun <T> LiveData<T>.observeNullSafe(owner: LifecycleOwner, observer: (t: T) -> Unit) {
    this.observe(owner, {
        it?.let(observer)
    })
}