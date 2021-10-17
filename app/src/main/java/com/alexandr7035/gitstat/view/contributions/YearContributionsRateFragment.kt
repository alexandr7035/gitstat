package com.alexandr7035.gitstat.view.contributions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.data.local.model.ContributionsYearWithRates
import com.alexandr7035.gitstat.databinding.FragmentYearContributionsRateBinding
import com.alexandr7035.gitstat.view.contributions.plots.LinePlotFill
import com.alexandr7035.gitstat.view.contributions.plots.contributions_rate.ContributionsRatePlot


class YearContributionsRateFragment : Fragment() {

    private var binding: FragmentYearContributionsRateBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentYearContributionsRateBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val yearData = arguments?.getSerializable("yearData") as ContributionsYearWithRates

        // Setup plot
        if (binding?.rateChart != null) {
            val plot = ContributionsRatePlot()
            plot.setupPLot(binding!!.rateChart)
            plot.setYearData(binding!!.rateChart, yearData)
        }

        // Set year title
        binding?.year?.text = yearData.year.id.toString()

        // Background for contribution rate views
        // Set same color as for the plot
        val plotFill = LinePlotFill.getPlotFillForYear(requireContext(), yearData.year.id)
        val bg = ContextCompat.getDrawable(requireContext(), R.drawable.background_rounded_shape)
        bg?.setTint(plotFill.lineColor)

        // Peak contribution rate
        val maxContributionsRate = yearData.contributionRates.maxByOrNull { it.rate }?.rate
        binding?.peakCRView?.background = bg
        binding?.peakCRView?.text = maxContributionsRate.toString()

        // Last contribution rate (end of the year)
        val lastContributionRate = yearData.contributionRates[yearData.contributionRates.size - 1].rate
        binding?.lastCRView?.background = bg
        binding?.lastCRView?.text = lastContributionRate.toString()

    }

}