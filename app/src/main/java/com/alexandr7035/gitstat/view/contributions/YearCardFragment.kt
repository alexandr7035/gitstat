package com.alexandr7035.gitstat.view.contributions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.data.local.model.ContributionsYearWithDays
import com.alexandr7035.gitstat.databinding.ViewPlotContributionsYearBinding
import com.alexandr7035.gitstat.view.contributions.plots.LinePlotFill
import com.alexandr7035.gitstat.view.contributions.plots.contributions_per_year.ContributionsCountPlot
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YearCardFragment: Fragment() {

    private var binding: ViewPlotContributionsYearBinding? = null
    private val viewModel by viewModels<ContributionsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ViewPlotContributionsYearBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val yearData = arguments?.getSerializable("yearData") as ContributionsYearWithDays

        // Set plot data
        val contributionsCountPlot = ContributionsCountPlot()
        if (binding?.contributionsChart != null) {
            contributionsCountPlot.setupPLot(binding!!.contributionsChart)
            contributionsCountPlot.setYearData(binding!!.contributionsChart, yearData)
        }

        // Set contributions count to cart title
        val contributionsCount = yearData.contributionDays.sumOf { it.count }
        binding?.contributionsCountView?.text = contributionsCount.toString()

        // Count contributions rate (for the year) and apply to the view
//        val contributionsRate = round((contributionsCount.toFloat() / yearData.contributionDays.size.toFloat() * 100)) / 100F
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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}