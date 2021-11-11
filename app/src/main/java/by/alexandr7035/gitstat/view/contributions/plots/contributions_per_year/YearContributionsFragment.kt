package by.alexandr7035.gitstat.view.contributions.plots.contributions_per_year

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.ViewPlotContributionsYearBinding
import by.alexandr7035.gitstat.extensions.debug
import by.alexandr7035.gitstat.extensions.setChartData
import by.alexandr7035.gitstat.extensions.setupYAxisValuesForContributions
import by.alexandr7035.gitstat.extensions.setupYearLineChartView
import by.alexandr7035.gitstat.view.contributions.ContributionsViewModel
import by.alexandr7035.gitstat.view.contributions.plots.DateMonthsValueFormatter
import by.alexandr7035.gitstat.view.contributions.plots.LinePlotFill
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class YearContributionsFragment: Fragment() {

    private var binding: ViewPlotContributionsYearBinding? = null
    private val viewModel by viewModels<ContributionsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ViewPlotContributionsYearBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val year = arguments?.getInt("year")

        viewModel.getContributionYearsWithDaysLiveData().observe(viewLifecycleOwner, { yearsData ->

            Timber.debug("$year $yearsData")

            if (! yearsData.isNullOrEmpty()) {

                // FIXME find better solution (obtain certain year from data layer)
                val yearData = yearsData.findLast {
                    it.year.id == year
                }!!

                // Set contributions count to cart title
                val contributionsCount = yearData.contributionDays.sumOf { it.count }
                binding?.contributionsCountView?.text = contributionsCount.toString()

                // Get contributions rate (for the year) and apply to the view
                val contributionsRate = viewModel.getContributionRateForYear(yearData)
                // Set same color to the rate view as for the plot
                val bg = ContextCompat.getDrawable(requireContext(), R.drawable.background_rounded_shape)
                val plotFill = LinePlotFill.getPlotFillForYear(requireContext(), yearData.year.id)
                bg?.setTint(plotFill.lineColor)
                binding?.contributionsRateView?.background = bg
                binding?.contributionsRateView?.text = contributionsRate.toString()


                // Show stub if no contributions for this year
                if (contributionsCount == 0) {
                    binding?.emptyPlotStub?.visibility = View.VISIBLE

                    binding?.contributionsChart?.visibility = View.GONE
                    binding?.contributionsRateView?.visibility = View.GONE
                    binding?.contributionsCountView?.visibility = View.GONE
                    binding?.yearCRLabel?.visibility = View.GONE
                }

                // Prepare data for plot
                // TODO move out of fragment
                val entries = ArrayList<Entry>()
                yearData.contributionDays.forEach { contributionDay ->
                    entries.add(Entry(contributionDay.date.toFloat(), contributionDay.count.toFloat()))
                }
                val dataset = LineDataSet(entries, "")

                // Setup plot
                binding?.contributionsChart?.setupYearLineChartView(
                    xValueFormatter = DateMonthsValueFormatter(),
                    yValueFormatter = null
                )

                // Populate plot with data
                binding?.contributionsChart?.setChartData(
                    dataset,
                    LinePlotFill.getPlotFillForYear(requireContext(), yearData.year.id)
                )

                // Setup left axis
                binding?.contributionsChart?.axisLeft?.setupYAxisValuesForContributions(
                    yearData.contributionDays.maxByOrNull { it.count }?.count ?: 0
                )
                // Update plot
                binding?.contributionsChart?.invalidate()
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}