package com.alexandr7035.gitstat.view.repositories_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexandr7035.gitstat.core.Language
import com.alexandr7035.gitstat.data.Repository
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.alexandr7035.gitstat.view.repositories_list.filters.ReposFilters
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RepositoriesListViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    fun syncRepositoriesCache() {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.getRepositoriesLiveDataFromCache(repositoriesLiveData)
//        }
    }

    fun getAllRepositoriesListLiveData(): LiveData<List<RepositoryEntity>> {
        return repository.getAllRepositoriesLiveData()
    }

    fun getActiveRepositoriesLiveData(): LiveData<List<RepositoryEntity>> {
        return repository.getActiveRepositoriesLiveData()
    }

    fun getArchivedRepositoriesLiveData(): LiveData<List<RepositoryEntity>> {
        return repository.getArchivedRepositoriesLiveData()
    }

    fun getLanguagesForReposList(repos: List<RepositoryEntity>): List<Language> {
        return repository.getLanguagesForReposList(repos)
    }


    fun getRepositoriesFilters(): ReposFilters {
        return repository.getRepositoriesFilters()
    }

    fun saveRepositoriesFilters(filters: ReposFilters) {
        repository.saveRepositoriesFilters(filters)
    }
}
