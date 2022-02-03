package by.alexandr7035.gitstat.view.core

// Use it for fields that may be empty
// and therefore can be hidden on UI
data class HideableField<T>(
    val value: T,
    val isVisible: Boolean
)
