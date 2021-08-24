package com.alexandr7035.gitstat.data.remote.model

data class UserModel(
    val id: Long,
    val login: String,
    val avatar_url: String,
    val name: String,
    val location: String,
    val public_repos: Long,
    val total_private_repos: Long,

    val followers: Long,
    val following: Long,

    val created_at: String,
    val updated_at: String
)
