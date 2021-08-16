package com.example.gitstat.data.remote

import android.telephony.cdma.CdmaCellLocation

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