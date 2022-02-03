package by.alexandr7035.gitstat.view.profile

import by.alexandr7035.gitstat.view.core.HideableField

data class ProfileUi(
    val id: String,
    val login: String,
    val name: HideableField<String>,
    val avatar_url: String,
    val location: HideableField<String>,
    val followers: String,
    val created_at: String,
    val updated_at: String
)