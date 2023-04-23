package by.alexandr7035.gitstat.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey


// The real number of contributions obtained from summarizing contribution days may differ
// from sum of them in ContributionTypesEntity due to API behaviour
// Some contributions are not included in any of 5 types (commits, issues, PRs, reviews, repositories) returned from API
// E.g the first contributions - account creation. Or contributions from deleted repos in some cases
// So we pass realTotalContributions in order to calculate the difference
// and store it in "unknown" field
@Entity(tableName = "contribution_types")
data class ContributionTypesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val pullRequestReviewContributions: Int,
    val issueContributions: Int,
    val pullRequestContributions: Int,
    val repositoryContributions: Int,
    val commitContributions: Int,
    val unknownContributions: Int,

    val totalContributions: Int,
    val yearId: Int
)