package com.alexandr7035.gitstat.view.repositories.filters

import com.alexandr7035.gitstat.data.local.model.RepositoryEntity

object RepositoriesListFiltersHelper {

    fun getFilteredRepositoriesList(unfilteredList: List<RepositoryEntity>, filters: ReposFilters): List<RepositoryEntity> {
        //Log.d("DEBUG_TAG", "apply $filters")

        val filteredList = ArrayList<RepositoryEntity>()

        // Filtering
        // Private / public repos
        when (filters.filterPrivacy) {
            ReposFilters.FilterPrivacy.PUBLIC_REPOS_ONLY -> unfilteredList.forEach {
                if (! it.isPrivate) {
                    filteredList.add(it)
                }
            }

            ReposFilters.FilterPrivacy.PRIVATE_REPOS_ONLY -> unfilteredList.forEach {
                if (it.isPrivate) {
                    filteredList.add(it)
                }
            }

            else -> filteredList.addAll(unfilteredList)
        }

        // Forks
        when (filters.filterForks) {
            ReposFilters.FilterForks.FORKS_ONLY -> unfilteredList.forEach {
                if (! it.fork) {
                    filteredList.remove(it)
                }
            }

            ReposFilters.FilterForks.EXCLUDE_FORKS -> unfilteredList.forEach {
                if (it.fork) {
                    filteredList.remove(it)
                }
            }

            ReposFilters.FilterForks.ALL_REPOS -> true
        }

        // Remove all repos if language is not from filters' set
        if (filters.filterLanguages.isNotEmpty()) {
            filteredList.removeAll {
                !filters.filterLanguages.contains(it.language)
            }
        }

        // Sorting
        when (filters.sortingType) {
            ReposFilters.SortingType.BY_REPO_NAME -> {
                when (filters.sortingOrder) {
                    ReposFilters.SortingOrder.ASCENDING_MODE -> RepositoriesSorter.sortByRepoNameAscending(filteredList)
                    ReposFilters.SortingOrder.DESCENDING_MODE -> RepositoriesSorter.sortByRepoNameDescending(filteredList)
                }
            }

            ReposFilters.SortingType.BY_REPO_CREATION_DATE -> {
                when (filters.sortingOrder) {
                    ReposFilters.SortingOrder.ASCENDING_MODE -> RepositoriesSorter.sortByRepoCreationDateAscending(filteredList)
                    ReposFilters.SortingOrder.DESCENDING_MODE -> RepositoriesSorter.sortByRepoCreationDateDescending(filteredList)
                }
            }
        }

        return filteredList
    }
}