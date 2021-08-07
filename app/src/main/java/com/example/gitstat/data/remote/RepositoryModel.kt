package com.example.gitstat.data.remote

data class RepositoryModel(
    val id: Long,
    val name: String,
    val private: Boolean,
    var language: String,
    val open_issues: Long,
    val created_at: String
)