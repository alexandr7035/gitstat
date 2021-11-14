package by.alexandr7035.gitstat.view.contributions_grid

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import by.alexandr7035.gitstat.data.ContributionsRepository
import by.alexandr7035.gitstat.data.local.model.ContributionYearWithMonths
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContributionsGridViewModel @Inject constructor(private val repository: ContributionsRepository): ViewModel() {

    fun getContributionYearWithMonths(yearId: Int): LiveData<ContributionYearWithMonths> {
        return repository.getContributionYearWithMonthsLiveData(yearId)
    }
}