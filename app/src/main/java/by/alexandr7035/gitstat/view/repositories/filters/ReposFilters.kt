package by.alexandr7035.gitstat.view.repositories.filters

// Defaults in the constructor
data class ReposFilters(
    var sortingType: SortingType = SortingType.BY_REPO_NAME,
    var sortingOrder: SortingOrder = SortingOrder.ASCENDING_MODE,
    var filterPrivacy: FilterPrivacy = FilterPrivacy.ALL_REPOS,
    var filterForks: FilterForks = FilterForks.ALL_REPOS,
    var filterLanguages: Set<String> = HashSet()
)

{
    enum class SortingType {
        BY_REPO_NAME,
        BY_REPO_CREATION_DATE
    }

    enum class SortingOrder {
        ASCENDING_MODE,
        DESCENDING_MODE
    }

    enum class FilterPrivacy {
        PRIVATE_REPOS_ONLY,
        PUBLIC_REPOS_ONLY,
        ALL_REPOS
    }

    enum class FilterForks {
        FORKS_ONLY,
        EXCLUDE_FORKS,
        ALL_REPOS
    }
}