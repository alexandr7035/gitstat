package com.alexandr7035.gitstat.data

import androidx.lifecycle.LiveData
import com.alexandr7035.gitstat.data.local.dao.ContributionsDao
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import com.alexandr7035.gitstat.data.local.model.ContributionsRatioEntity
import com.alexandr7035.gitstat.data.local.model.ContributionsYearWithDays
import com.alexandr7035.gitstat.data.local.model.ContributionsYearWithRates
import javax.inject.Inject

// FIXME use interface
class ContributionsRepository @Inject constructor(private val dao: ContributionsDao) {

    fun getAllContributionsLiveData(): LiveData<List<ContributionDayEntity>> {
        return dao.getContributionDaysLiveData()
    }

    fun getContributionYearsWithDaysLiveData(): LiveData<List<ContributionsYearWithDays>> {
        return dao.getContributionYearsWithDaysLiveData()
    }

    fun getContributionYearsWithRatesLiveData(): LiveData<List<ContributionsYearWithRates>> {
        return dao.getContributionYearsWithRatesCache()
    }

    fun getContributionsRatioLiveData(): LiveData<List<ContributionsRatioEntity>> {
        return dao.getContributionRatiosLiveData()
    }
}