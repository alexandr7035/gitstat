package com.alexandr7035.gitstat.view.contributions

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.data.local.model.ContributionsYearWithRates
import com.alexandr7035.gitstat.databinding.FragmentYearContributionsRateBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class YearContributionsRateFragment : Fragment() {

    private var binding: FragmentYearContributionsRateBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentYearContributionsRateBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Plot setup
        binding!!.contributionsChart.apply {

            // Disable legend
            legend.form = Legend.LegendForm.NONE

            setScaleEnabled(false)

            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.textSize = 16f
            // Show months names
            xAxis.valueFormatter = DateMonthsValueFormatter()

            axisRight.setDrawLabels(false)
            axisRight.axisMinimum = 0f
            axisRight.setDrawGridLines(false)

            axisLeft.setDrawGridLines(false)
            axisLeft.axisMinimum = 0f
            axisLeft.textSize = 16f

            description.isEnabled = false
        }

        val yearData = arguments?.getSerializable("yearData") as ContributionsYearWithRates
        Log.d("DEBUG_TAG", "years data rates $yearData")

        yearData.apply {
            binding?.year?.text = this.year.id.toString()

            val entries = ArrayList<Entry>()
            this.contributionRates.forEach { contributionRate ->
                entries.add(Entry(contributionRate.date.toFloat(), contributionRate.rate.toFloat()))
            }

            val dataset = LineDataSet(entries, "")

            // Fill only from zero point
            dataset.fillFormatter = IFillFormatter { dataSet, dataProvider -> 0f }
            val plotFill = PlotFill.getPlotFillForYear(requireContext(), yearData.year.id)

            dataset.apply {
                setDrawFilled(true)
                setDrawCircles(false)
                setDrawValues(false)

                color = plotFill.lineColor
                fillDrawable = plotFill.fillDrawable
            }

            val lineData = LineData(dataset)

            val maxRateValue = contributionRates.maxByOrNull { it.rate }?.rate
            val yAxisParams = RateYAxisParams.getRateYAxisParams(maxRateValue ?: 10f)

            binding!!.contributionsChart.apply {
                data = lineData
                data.isHighlightEnabled = false

                // Update axis params
                axisLeft.valueFormatter = yAxisParams.yAxisValueFormatter
                axisLeft.axisMinimum = yAxisParams.minValue
                axisLeft.axisMaximum = yAxisParams.maxValue
                axisLeft.setLabelCount(yAxisParams.labelCount, true)
            }

            // Update chart
            binding?.contributionsChart?.invalidate()


            val maxContributionsRate = contributionRates.maxByOrNull { it.rate }

            val bg = ContextCompat.getDrawable(requireContext(), R.drawable.background_rounded_shape)
            bg?.setTint(plotFill.lineColor)
            binding?.contributionsRateView?.background = bg
            binding?.contributionsRateView?.text = getString(
                R.string.contributions_rate,
                maxContributionsRate?.rate
            )

//            // Show stub if no contributions for this year
//            if (contributionsCount == 0) {
//                binding?.emptyPlotStub?.visibility = View.VISIBLE
//                binding?.contributionsChart?.visibility = View.GONE
//                // Invisible as year position depends on them
//                binding?.contributionsRateView?.visibility = View.INVISIBLE
//                binding?.contributionsCountView?.visibility = View.INVISIBLE
//            }

        }


    }


    // TODO: DRY
    inner class DateMonthsValueFormatter: ValueFormatter() {
        override fun getFormattedValue(value: Float): String {

            // Example: convert "2020-02" to Feb
            val format = SimpleDateFormat("MMM", Locale.US)
            format.timeZone = TimeZone.getTimeZone("GMT")

            return format.format(value.toLong()).toString()
        }
    }
}