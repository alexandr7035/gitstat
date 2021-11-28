package by.alexandr7035.gitstat.view.contributions.contribution_year

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.FragmentContributionYearBinding
import by.alexandr7035.gitstat.extensions.debug
import by.alexandr7035.gitstat.extensions.setChartData
import by.alexandr7035.gitstat.extensions.setupYAxisValuesForContributionRate
import by.alexandr7035.gitstat.extensions.setupYearLineChartView
import by.alexandr7035.gitstat.view.contributions.ContributionsViewModel
import by.alexandr7035.gitstat.view.contributions.plots.DateMonthsValueFormatter
import by.alexandr7035.gitstat.view.contributions.plots.LinePlotFill
import by.alexandr7035.gitstat.view.contributions.plots.contributions_rate.ContributionRateYValueFormatter
import by.alexandr7035.gitstat.view.repositories.RepositoriesFragmentsAdapter
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class ContributionYearFragment : Fragment() {

    private var binding: FragmentContributionYearBinding? = null
    private val viewModel by viewModels<ContributionsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentContributionYearBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val year = arguments?.getInt("year")

        Timber.debug("selected year $year")

//        binding?.textVeiw?.text = year.toString()

        // Observe the result
        viewModel.getContributionYearsWithRatesLiveData().observe(viewLifecycleOwner, { yearsData ->

            if (! yearsData.isNullOrEmpty()) {

                // FIXME find better solution (obtain certain year from data layer)
                val yearData = yearsData.findLast {
                    it.year.id == year
                }!!

                // Background for contribution rate views
                // Set same color as for the plot
                val plotFill = LinePlotFill.getPlotFillForYear(requireContext(), yearData.year.id)
                val bg = ContextCompat.getDrawable(requireContext(), R.drawable.background_rounded_shape)
                bg?.setTint(plotFill.lineColor)

                // Peak contribution rate
                val maxContributionsRate = viewModel.getMaxContributionRateForYear(yearData)
                binding?.peakCRView?.background = bg
                binding?.peakCRView?.text = maxContributionsRate.toString()

                // Last contribution rate (end of the year)
                val lastContributionRate = viewModel.getLastTotalContributionRateForYear(yearData)
                binding?.lastCRView?.background = bg
                binding?.lastCRView?.text = lastContributionRate.toString()


                // Prepare plot dataset
                // TODO move out of fragment
                val entries = ArrayList<Entry>()
                yearData.contributionRates.forEach { contributionRate ->
                    entries.add(Entry(contributionRate.date.toFloat(), contributionRate.rate))
                }
                val dataset = LineDataSet(entries, "")

                // Setup plot
                binding?.rateChart?.setupYearLineChartView(
                    xValueFormatter = DateMonthsValueFormatter(),
                    yValueFormatter = ContributionRateYValueFormatter(0f, maxContributionsRate)
                )
                binding?.rateChart?.setExtraOffsets(0f,0f,0f,0f)

                // Setup left axis
                binding?.rateChart?.axisLeft?.setupYAxisValuesForContributionRate(topValue = maxContributionsRate)

                // Populate plot with data
                binding?.rateChart?.setChartData(
                    dataset,
                    LinePlotFill.getPlotFillForYear(requireContext(), yearData.year.id)
                )

                // Update data
                binding?.rateChart?.invalidate()
            }
        })
    }
}