package by.alexandr7035.gitstat.view.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.gitstat.core.Language
import by.alexandr7035.gitstat.data.ReposRepository
import by.alexandr7035.gitstat.data.local.model.RepositoryEntity
import by.alexandr7035.gitstat.view.repositories.filters.ReposFilters
import by.alexandr7035.gitstat.view.repositories.filters.RepositoriesListFiltersHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoriesViewModel @Inject constructor(private val repository: ReposRepository): ViewModel() {

    private val allRepositoriesLiveData = MutableLiveData<List<RepositoryEntity>>()
    private val activeReposLiveData = MutableLiveData<List<RepositoryEntity>>()
    private val archivedReposLiveData = MutableLiveData<List<RepositoryEntity>>()
    private val tabRefreshedLiveData = MutableLiveData<Int>()

    fun getRepositoriesLiveData(): LiveData<List<RepositoryEntity>> {
        return repository.getRepositoriesLiveData()
    }

    fun getRepositoryLiveData(repositoryId: Int): LiveData<RepositoryEntity> {
        return repository.getRepositoryLiveData(repositoryId)
    }

    // Repos livedata
    fun getAllRepositoriesListLiveData(): LiveData<List<RepositoryEntity>> {
        return allRepositoriesLiveData
    }

    fun getActiveRepositoriesLiveData(): LiveData<List<RepositoryEntity>> {
        return activeReposLiveData
    }

    fun getArchivedRepositoriesLiveData(): LiveData<List<RepositoryEntity>> {
        return archivedReposLiveData
    }


    // We need to update livedata manually
    // as when dialog with filters is closed no lifecycle method of parent fragment is called
    // and list stays not updated
    // TODO find better solution
    fun updateActiveRepositoriesLiveData() {
        viewModelScope.launch(Dispatchers.IO) {
            activeReposLiveData.postValue(repository.fetchActiveRepositoriesFromDb())
        }
    }

    fun updateArchivedRepositoriesLiveData() {
        viewModelScope.launch(Dispatchers.IO) {
            archivedReposLiveData.postValue(repository.fetchArchivedRepositoriesFromDb())
        }
    }

    fun updateAllRepositoriesLiveData() {
        viewModelScope.launch(Dispatchers.IO) {
            allRepositoriesLiveData.postValue(repository.fetchAllRepositoriesFromDb())
        }
    }


    // Filters and sorting
    fun getRepositoriesFilters(): ReposFilters {
        return repository.getRepositoriesFilters()
    }

    fun saveRepositoriesFilters(filters: ReposFilters) {
        repository.saveRepositoriesFilters(filters)
    }

    fun getFilteredRepositoriesList(repos: List<RepositoryEntity>): List<RepositoryEntity> {
        return RepositoriesListFiltersHelper.getFilteredRepositoriesList(repos, repository.getRepositoriesFilters())
    }


    // Languages
    fun getLanguagesForReposList(repos: List<RepositoryEntity>): List<Language> {
        return repository.getLanguagesForReposList(repos)
    }


    // For scrolling recyclers up when tab is refreshed
    // Takes tab position as argument
    fun getTabRefreshedLiveData(): LiveData<Int> {
        return tabRefreshedLiveData
    }

    fun refreshTabRecycler(position: Int) {
        tabRefreshedLiveData.value = position
    }

}
