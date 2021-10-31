package by.alexandr7035.gitstat.core

object GithubAccessScopes {

    fun getScopes(): List<String> {

        return listOf(
            "read:user",
            "repo"
        )
    }
}