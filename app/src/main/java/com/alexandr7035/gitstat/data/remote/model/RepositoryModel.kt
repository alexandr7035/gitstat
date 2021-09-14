package com.alexandr7035.gitstat.data.remote.model

data class RepositoryModel(
    val id: Long,
    val name: String,
    val private: Boolean,
    var fork: Boolean,
    var language: String,
    val open_issues: Long,
    val created_at: String,
    val stargazers_count: Long
)