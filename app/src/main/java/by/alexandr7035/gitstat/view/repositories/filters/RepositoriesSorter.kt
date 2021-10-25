package by.alexandr7035.gitstat.view.repositories.filters

import by.alexandr7035.gitstat.data.local.model.RepositoryEntity
import java.util.*

object RepositoriesSorter {
    fun sortByRepoNameAscending(repos: List<RepositoryEntity>) {
        Collections.sort(repos
        ) { o1, o2 -> o1.name.lowercase(Locale.getDefault()).compareTo(o2.name.lowercase(Locale.getDefault())) }
    }

    fun sortByRepoNameDescending(repos: List<RepositoryEntity>) {
        Collections.sort(repos
        ) { o1, o2 -> -1 * o1.name.lowercase(Locale.getDefault()).compareTo(o2.name.lowercase(Locale.getDefault())) }
    }

    fun sortByRepoCreationDateAscending(repos: List<RepositoryEntity>) {
        Collections.sort(repos
        ) { o1, o2 -> o1.created_at.compareTo(o2.created_at) }
    }

    fun sortByRepoCreationDateDescending(repos: List<RepositoryEntity>) {
        Collections.sort(repos
        ) { o1, o2 -> -1 * o1.created_at.compareTo(o2.created_at) }
    }
}