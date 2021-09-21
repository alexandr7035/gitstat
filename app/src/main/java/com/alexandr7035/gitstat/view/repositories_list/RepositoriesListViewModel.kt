package com.alexandr7035.gitstat.view.repositories_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandr7035.gitstat.core.Language
import com.alexandr7035.gitstat.data.Repository
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.alexandr7035.gitstat.view.repositories_list.filters.ReposFilters
import com.alexandr7035.gitstat.view.repositories_list.filters.RepositoriesListFiltersHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoriesListViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val allRepositoriesLiveData = MutableLiveData<List<RepositoryEntity>>()
    private val activeReposLiveData = MutableLiveData<List<RepositoryEntity>>()
    private val archivedReposLiveData = MutableLiveData<List<RepositoryEntity>>()



//    fun getActiveRepositoriesLiveData(): LiveData<List<RepositoryEntity>> {
//
//        return repository.getActiveRepositoriesLiveData()
//    }

//    fun getArchivedRepositoriesLiveData(): LiveData<List<RepositoryEntity>> {
//        return repository.getArchivedRepositoriesLiveData()
//    }

    fun getLanguagesForReposList(repos: List<RepositoryEntity>): List<Language> {
        return repository.getLanguagesForReposList(repos)
    }


    fun getRepositoriesFilters(): ReposFilters {
        return repository.getRepositoriesFilters()
    }

    fun saveRepositoriesFilters(filters: ReposFilters) {
        repository.saveRepositoriesFilters(filters)
    }


    fun updateActiveRepositoriesLiveData() {
        viewModelScope.launch(Dispatchers.IO) {
            val repos = repository.fetchActiveRepositoriesFromDb()
            val filteredRepos = RepositoriesListFiltersHelper.getFilteredRepositoriesList(repos, repository.getRepositoriesFilters())
            activeReposLiveData.postValue(filteredRepos)
        }
    }

    fun updateArchivedRepositoriesLiveData() {
        viewModelScope.launch(Dispatchers.IO) {
            val repos = repository.fetchArchivedRepositoriesFromDb()
            val filteredRepos = RepositoriesListFiltersHelper.getFilteredRepositoriesList(repos, repository.getRepositoriesFilters())
            archivedReposLiveData.postValue(filteredRepos)
        }
    }

    fun updateAllRepositoriesLiveData() {
        viewModelScope.launch(Dispatchers.IO) {
            val repos = repository.fetchAllRepositoriesFromDb()
            val filteredRepos = RepositoriesListFiltersHelper.getFilteredRepositoriesList(repos, repository.getRepositoriesFilters())
            allRepositoriesLiveData.postValue(filteredRepos)
        }
    }

    fun getAllRepositoriesListLiveData(): LiveData<List<RepositoryEntity>> {
        return allRepositoriesLiveData
    }

    fun getActiveRepositoriesLiveData(): LiveData<List<RepositoryEntity>> {
        return activeReposLiveData
    }

    fun getArchivedRepositoriesLiveData(): LiveData<List<RepositoryEntity>> {
        return archivedReposLiveData
    }

}
