package by.alexandr7035.gitstat.core

enum class DataSyncStatus {
    PENDING_PROFILE,
    PENDING_REPOSITORIES,
    PENDING_CONTRIBUTIONS,
    SUCCESS,
    FAILED_NETWORK,
    AUTHORIZATION_ERROR
}