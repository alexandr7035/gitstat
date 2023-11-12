package by.alexandr7035.gitstat.core

sealed class DataSyncStatus {
    object PendingProfile: DataSyncStatus()
    object PendingRepos: DataSyncStatus()
    object PendingContributions: DataSyncStatus()
    data class Failure(val error: ErrorType): DataSyncStatus()
    object Success: DataSyncStatus()
}