package by.alexandr7035.gitstat.core

object GithubAccessScopes {
    val BASIC_SCOPES = listOf(
        "read:user",
    )
    val EXTENDED_SCOPES = listOf(
        "read:user",
        "repo"
    )
}