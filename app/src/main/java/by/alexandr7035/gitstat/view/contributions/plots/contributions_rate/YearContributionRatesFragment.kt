package by.alexandr7035.gitstat.view.contributions.plots.contributions_rate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.FragmentYearContributionsRateBinding
import by.alexandr7035.gitstat.extensions.setChartData
import by.alexandr7035.gitstat.extensions.setupYAxisValuesForContributionRate
import by.alexandr7035.gitstat.extensions.setupYearLineChartView
import by.alexandr7035.gitstat.view.contributions.ContributionsViewModel
import by.alexandr7035.gitstat.view.contributions.plots.DateMonthsValueFormatter
import by.alexandr7035.gitstat.view.contributions.plots.LinePlotFill
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YearContributionRatesFragment : Fragment() {

    private var binding: FragmentYearContributionsRateBinding? = null
    private val viewModel by viewModels<ContributionsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentYearContributionsRateBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val year = arguments?.getInt("year")

        // Observe the result
        viewModel.getContributionYearsWithRatesLiveData().observe(viewLifecycleOwner, { yearsData ->

            if (! yearsData.isNullOrEmpty()) {

                // FIXME find better solution (obtain certain year from data layer)
                val yearData = yearsData.findLast {
                    it.year.id == year
                }!!

                // Set year title
                binding?.year?.text = yearData.year.id.toString()

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
                binding?.rateChart?.setExtraOffsets(10f,0f,10f,0f)

                // Populate plot with data
                binding?.rateChart?.setChartData(
                    dataset,
                    LinePlotFill.getPlotFillForYear(requireContext(), yearData.year.id)
                )

                // Setup left axis
                binding?.rateChart?.axisLeft?.setupYAxisValuesForContributionRate(topValue = maxContributionsRate)
                // Update data
                binding?.rateChart?.invalidate()
            }
        })

    }

}