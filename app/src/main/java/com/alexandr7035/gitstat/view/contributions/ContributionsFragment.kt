package com.alexandr7035.gitstat.view.contributions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.databinding.FragmentContributionsBinding
import com.apollographql.apollo3.ApolloClient
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
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

        viewModel.test()

        var counter = 0
        val startTime = 1609459200000
        val endTime = 1640908800000
        var curr = startTime

        val days: ArrayList<ContributionDay> = ArrayList()
        while (curr <= endTime) {
            counter += 1

            days.add(ContributionDay(date = curr, count = (0..25).random()))

            curr += 86400000
        }


        val entries = ArrayList<Entry>()
        days.forEach {
            entries.add(Entry(it.date.toFloat(), it.count.toFloat()))
        }

        val dataset = LineDataSet(entries, "")
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

            // Disable legend
            legend.form = Legend.LegendForm.NONE

            setScaleEnabled(false)

            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.textSize = 16f
            // Show months names
            xAxis.valueFormatter = DateMonthsValueFormatter()

            axisRight.setDrawLabels(false)
            axisRight.setDrawGridLines(false)

            axisLeft.setDrawGridLines(false)
            axisLeft.textSize = 16f

            description.isEnabled = false
            data.isHighlightEnabled = false
        }
    }

    inner class DateMonthsValueFormatter: ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            format.timeZone = TimeZone.getTimeZone("GMT")

            val unixDate = SimpleDateFormat("MMM", Locale.US).format(value)

            return unixDate.toString()
        }
    }

    data class ContributionDay(val date: Long, val count: Int)

}