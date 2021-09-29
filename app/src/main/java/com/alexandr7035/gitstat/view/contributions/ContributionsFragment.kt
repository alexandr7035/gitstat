package com.alexandr7035.gitstat.view.contributions

import android.os.Bundle
import android.text.format.DateFormat
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
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ContributionsFragment : Fragment() {

    private var binding: FragmentContributionsBinding? = null
    private val viewModel by viewModels<ContributionsViewModel>()

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
        viewModel.getContributions().observe(viewLifecycleOwner, { contributions ->

            if (contributions.isNotEmpty()) {

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

        viewModel.syncContributions()

    }

    inner class DateMonthsValueFormatter: ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            val date = DateFormat.format("MMM", value.toLong())
            return date.toString()
        }
    }

}