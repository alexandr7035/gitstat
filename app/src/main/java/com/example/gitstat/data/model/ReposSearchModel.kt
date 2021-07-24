package com.example.gitstat.data.model

data class ReposSearchModel(
    val total_count: Long,
    val incomplete_results: Boolean,
    val items: List<RepositoryModel>
)