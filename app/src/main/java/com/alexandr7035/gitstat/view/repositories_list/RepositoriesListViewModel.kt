package com.alexandr7035.gitstat.view.repositories_list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.alexandr7035.gitstat.view.repositories_list.filters.ReposFilters

class RepositoriesListViewModel(application: Application): AndroidViewModel(application) {

    private val filtersLiveData = MutableLiveData<ReposFilters>()

    private var counter = 0

    fun getFilters(): MutableLiveData<ReposFilters> {
        return filtersLiveData
    }

    fun getActiveRepositories() {

    }

    fun testScope() {
        counter += 1
        Log.d("DEBUG_TAG", "counter $counter")
    }
}
