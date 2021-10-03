package com.alexandr7035.gitstat.data

import androidx.lifecycle.LiveData
import com.alexandr7035.gitstat.data.local.CacheDao
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import javax.inject.Inject

// FIXME use interface
class ContributionsRepository @Inject constructor(
    private val dao: CacheDao) {

    fun getAllContributions(): LiveData<List<ContributionDayEntity>> {
        return dao.getContributionsDaysCache()
    }

}