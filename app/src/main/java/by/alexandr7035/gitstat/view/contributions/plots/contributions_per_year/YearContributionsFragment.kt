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
import by.alexandr7035.gitstat.extensions.setupContributionsChartData
import by.alexandr7035.gitstat.extensions.setupYearLineChartView
import by.alexandr7035.gitstat.view.contributions.ContributionsViewModel
import by.alexandr7035.gitstat.view.contributions.plots.DateMonthsValueFormatter
import by.alexandr7035.gitstat.view.contributions.plots.LinePlotFill
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

                // Setup plot
                binding?.contributionsChart?.setupYearLineChartView(DateMonthsValueFormatter())
                // Populate plot with data
                binding?.contributionsChart?.setupContributionsChartData(
                    yearData,
                    LinePlotFill.getPlotFillForYear(requireContext(), yearData.year.id)
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