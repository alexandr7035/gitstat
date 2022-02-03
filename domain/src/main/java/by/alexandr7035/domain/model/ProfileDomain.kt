package by.alexandr7035.domain.model

data class ProfileDomain(
    val id: Int,
    val login: String,
    val name: String,
    val avatar_url: String,
    val location: String,
    val followers: Int,
    val following: Int,
    val created_at: Long,
    val updated_at: Long
)
