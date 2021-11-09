package by.alexandr7035.gitstat.data.remote.mappers

import by.alexandr7035.gitstat.apollo.ContributionsQuery
import by.alexandr7035.gitstat.core.Mapper
import by.alexandr7035.gitstat.core.TimeHelper
import by.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import by.alexandr7035.gitstat.data.local.model.ContributionRateEntity
import timber.log.Timber
import kotlin.math.round

class ContributionDaysToRatesMapper(private val timeHelper: TimeHelper): Mapper<List<ContributionDayEntity>, List<ContributionRateEntity>> {
    override fun transform(data: List<ContributionDayEntity>): List<ContributionRateEntity> {

        val rates = ArrayList<ContributionRateEntity>()

        data.forEachIndexed { position, day ->
            val daysSlice = data.slice(0..position)
            val daysSliceContributionsCount = daysSlice.sumOf { it.count }

            // Don't count rate for the future :)
            // Set 0 by default
            // TODO check with changing timezones
            var rate = 0F
            if (day.date <= timeHelper.getBeginningOfDayForUnixDate(System.currentTimeMillis())) {
                rate = round((daysSliceContributionsCount.toFloat() / daysSlice.size.toFloat() * 100)) / 100F
            }

            Timber.d("count " + daysSliceContributionsCount.toFloat() + " / " + daysSlice.size.toFloat() + " * 100 / 100f")

            rates.add(
                ContributionRateEntity(
                    date = day.date,
                    rate = rate,
                    yearId = day.yearId
                )
            )
        }

        return rates
    }
}