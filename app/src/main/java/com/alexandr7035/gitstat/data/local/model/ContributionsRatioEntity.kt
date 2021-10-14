package com.alexandr7035.gitstat.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contributions_ratio")
data class ContributionsRatioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val totalPullRequestReviewContributions: Int,
    val totalIssueContributions: Int,
    val totalPullRequestContributions: Int,
    val totalRepositoryContributions: Int,
    val totalCommitContributions: Int,
    val yearId: Int)