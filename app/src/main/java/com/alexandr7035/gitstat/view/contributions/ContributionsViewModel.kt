package com.alexandr7035.gitstat.view.contributions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandr7035.gitstat.data.ContributionsRepository
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import com.alexandr7035.gitstat.data.local.model.ContributionsYear
import com.alexandr7035.gitstat.data.local.model.ContributionsYearWithDays
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import javax.inject.Inject

@HiltViewModel
class ContributionsViewModel @Inject constructor(private val repository: ContributionsRepository): ViewModel() {

    private val contributionYearsLiveData = MutableLiveData<List<ContributionsYear>>()

    fun getContributionsLiveData(): LiveData<List<ContributionDayEntity>> {
        return repository.getAllContributionsLiveData()
    }

    fun fetchContributionYears() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getContributionYears()
            contributionYearsLiveData.postValue(data)
        }
    }

//    fun getContributionYearsLiveData(): MutableLiveData<List<ContributionsYear>> {
//        return contributionYearsLiveData
//    }

    fun getContributionYearsLiveData(): LiveData<List<ContributionsYearWithDays>> {
        return repository.getContributionYearsLiveData()
    }
}