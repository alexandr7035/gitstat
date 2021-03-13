package com.example.gitstat.model

import android.telephony.cdma.CdmaCellLocation

data class UserModel(
    val id: Long,
    val login: String,
    val avatar_url: String,
    val name: String,
    val location: String,
    val public_repos: Long,
    val total_private_repos: Long
)
