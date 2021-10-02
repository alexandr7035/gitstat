package com.alexandr7035.gitstat.view.contributions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.databinding.FragmentContributionsBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ContributionsFragment : Fragment() {

    private var binding: FragmentContributionsBinding? = null
    private val viewModel by viewModels<ContributionsViewModel>()

    private lateinit var adapter: YearContributionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentContributionsBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
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


        // Update data
        viewModel.getLastYearContributions().observe(viewLifecycleOwner, { contributions ->

            if (contributions.isNotEmpty()) {


                // FIXME test
                val yearsData = listOf<ContributionsYear>(
                    ContributionsYear(2018, contributions),
                    ContributionsYear(2019, contributions),
                    ContributionsYear(2020, contributions)
                )

                adapter = YearContributionsAdapter(this)
                adapter.setItems(yearsData)
                binding?.yearsViewPager?.adapter = adapter


                // FIXME

                Log.d("DEBUG_TAG", "viewmodel get $contributions")

                val entries = ArrayList<Entry>()
                contributions.forEach { contributionDay ->
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


                binding?.contributionsCountView?.text = getString(
                    R.string.contributions_count,
                    "Last year",
                    contributions.sumOf { it.count }.toString())
            }
        })

        viewModel.syncLastYearContributions()

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