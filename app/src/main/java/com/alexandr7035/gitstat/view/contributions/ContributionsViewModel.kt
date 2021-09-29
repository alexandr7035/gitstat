package com.alexandr7035.gitstat.view.contributions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandr7035.gitstat.data.ContributionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContributionsViewModel @Inject constructor(private val repository: ContributionsRepository): ViewModel() {
    fun test() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.test()
        }
    }

    fun getLastYearContributions() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getLastYearContributions()
        }
    }
}