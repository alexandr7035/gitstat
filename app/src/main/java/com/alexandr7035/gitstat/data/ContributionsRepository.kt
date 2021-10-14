package com.alexandr7035.gitstat.data

import androidx.lifecycle.LiveData
import com.alexandr7035.gitstat.data.local.CacheDao
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import com.alexandr7035.gitstat.data.local.model.ContributionsRatioEntity
import com.alexandr7035.gitstat.data.local.model.ContributionsYearWithDays
import com.alexandr7035.gitstat.data.local.model.ContributionsYearWithRates
import javax.inject.Inject

// FIXME use interface
class ContributionsRepository @Inject constructor(private val dao: CacheDao) {

    fun getAllContributionsLiveData(): LiveData<List<ContributionDayEntity>> {
        return dao.getContributionsDaysCacheLiveData()
    }

    fun getContributionYearsLiveData(): LiveData<List<ContributionsYearWithDays>> {
        return dao.getContributionYearsCache()
    }

    fun getContributionYearsWithRatesLiveData(): LiveData<List<ContributionsYearWithRates>> {
        return dao.getContributionYearsWithRatesCache()
    }

    fun getContributionsRatioLiveData(): LiveData<List<ContributionsRatioEntity>> {
        return dao.getContributionsRatioLiveData()
    }
}