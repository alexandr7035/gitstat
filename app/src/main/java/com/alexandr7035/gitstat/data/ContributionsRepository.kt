package com.alexandr7035.gitstat.data

import androidx.lifecycle.LiveData
import com.alexandr7035.gitstat.data.local.CacheDao
import com.alexandr7035.gitstat.data.local.mappers.ContributionsDaysToYearsMapper
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import com.alexandr7035.gitstat.data.local.model.ContributionsYear
import javax.inject.Inject

// FIXME use interface
class ContributionsRepository @Inject constructor(
    private val dao: CacheDao,
    private val mapper: ContributionsDaysToYearsMapper) {

    fun getAllContributionsLiveData(): LiveData<List<ContributionDayEntity>> {
        return dao.getContributionsDaysCacheLiveData()
    }

    suspend fun getContributionYears(): List<ContributionsYear> {
        return mapper.transform(dao.getContributionsDaysCache())
    }

}