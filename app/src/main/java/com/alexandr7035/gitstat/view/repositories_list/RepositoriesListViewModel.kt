package com.alexandr7035.gitstat.view.repositories_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandr7035.gitstat.core.Language
import com.alexandr7035.gitstat.core.SyncStatus
import com.alexandr7035.gitstat.data.ReposRepository
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.alexandr7035.gitstat.view.repositories_list.filters.ReposFilters
import com.alexandr7035.gitstat.view.repositories_list.filters.RepositoriesListFiltersHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoriesListViewModel @Inject constructor(private val repository: ReposRepository): ViewModel() {

    private val allRepositoriesLiveData = MutableLiveData<List<RepositoryEntity>>()
    private val activeReposLiveData = MutableLiveData<List<RepositoryEntity>>()
    private val archivedReposLiveData = MutableLiveData<List<RepositoryEntity>>()


    // Sync from remote source to the DB
    fun syncRepositoriesData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.syncRepositoriesData()
        }
    }

    fun getSyncStatusLiveData(): LiveData<SyncStatus> {
        return repository.getSyncStatusLiveData()
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

}
