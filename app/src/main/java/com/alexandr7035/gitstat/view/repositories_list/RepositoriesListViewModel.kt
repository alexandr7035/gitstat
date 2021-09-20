package com.alexandr7035.gitstat.view.repositories_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandr7035.gitstat.data.Repository
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.alexandr7035.gitstat.view.repositories_list.filters.ReposFilters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoriesListViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val filtersLiveData = MutableLiveData<ReposFilters>()


    fun getFilters(): MutableLiveData<ReposFilters> {
        return filtersLiveData
    }

    fun syncRepositoriesCache() {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.getRepositoriesLiveDataFromCache(repositoriesLiveData)
//        }
    }

    fun getActiveRepositoriesLiveData(): LiveData<List<RepositoryEntity>> {
        return repository.getActiveRepositoriesLiveData()
    }

    fun getArchivedRepositoriesLiveData(): LiveData<List<RepositoryEntity>> {
        return repository.getArchivedRepositoriesLiveData()
    }
}
