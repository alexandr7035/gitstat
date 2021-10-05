package com.alexandr7035.gitstat.view.contributions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.data.local.model.ContributionsYear
import com.alexandr7035.gitstat.databinding.ViewPlotContributionsYearBinding
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

class YearCardFragment: Fragment() {

    private var binding: ViewPlotContributionsYearBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = ViewPlotContributionsYearBinding.inflate(inflater, container, false)

        return binding?.root
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


        val yearData = arguments?.getSerializable("yearData") as ContributionsYear

        yearData.apply {
            binding?.contributionsYear?.text = this.year.toString()

            val entries = ArrayList<Entry>()
            this.days.forEach { contributionDay ->
                entries.add(Entry(contributionDay.date.toFloat(), contributionDay.count.toFloat()))
            }

            val dataset = LineDataSet(entries, "")

            // Fill only from zero point
            dataset.fillFormatter = IFillFormatter { dataSet, dataProvider -> 0f }

            dataset.apply {
                setDrawFilled(true)
                setDrawCircles(false)
                setDrawValues(false)

                color = ContextCompat.getColor(requireContext(), R.color.contributions_color)
                fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.background_contributions_graph)
            }

            val lineData = LineData(dataset)

            binding!!.contributionsChart.apply {
                data = lineData
                data.isHighlightEnabled = false
            }

            // Update chart
            binding?.contributionsChart?.invalidate()

            val contributionsCount = this.days.sumOf { it.count }
            binding?.contributionsCountView?.text = contributionsCount.toString()

            // Show stub if no contributions for this year
            if (contributionsCount == 0) {
                binding?.emptyPlotStub?.visibility = View.VISIBLE
                binding?.contributionsChart?.visibility = View.GONE
            }
            else {
                binding?.emptyPlotStub?.visibility = View.GONE
                binding?.contributionsChart?.visibility = View.VISIBLE
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }


    inner class DateMonthsValueFormatter: ValueFormatter() {
        override fun getFormattedValue(value: Float): String {

            // Example: convert "2020-02" to Feb
            val format = SimpleDateFormat("MMM", Locale.US)
            format.timeZone = TimeZone.getTimeZone("GMT")

            return format.format(value.toLong()).toString()
        }
    }

}