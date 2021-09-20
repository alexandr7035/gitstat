package com.alexandr7035.gitstat.view.repositories_list

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.alexandr7035.gitstat.data.Repository
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.alexandr7035.gitstat.view.repositories_list.filters.ReposFilters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RepositoriesListViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val filtersLiveData = MutableLiveData<ReposFilters>()

    private var counter = 0

    fun getFilters(): MutableLiveData<ReposFilters> {
        return filtersLiveData
    }


    fun testScope() {
        counter += 1
        Log.d("DEBUG_TAG", "counter $counter")
    }


    fun syncRepositoriesCache() {
        viewModelScope.launch(Dispatchers.IO) {
//            repository.getRepositoriesLiveDataFromCache(repositoriesLiveData)
        }
    }

    fun getActiveRepositoriesLiveData(): LiveData<List<RepositoryEntity>> {
        return repository.getActiveRepositoriesLiveData()
    }

//    fun getActiveRepositoriesLiveData(): LiveData<List<RepositoryEntity>> {
//        return repository.getActiveRepositoriesLiveData()
//    }
}
