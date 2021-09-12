package com.alexandr7035.gitstat.view.filters

// Defaults in the constructor
data class ReposFilters(
    var sortingType: SortingType = SortingType.BY_REPO_NAME,
    var sortingOrder: SortingOrder = SortingOrder.ASCENDING_MODE,
    var filterPrivacy: FilterPrivacy = FilterPrivacy.ALL_REPOS,
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
}