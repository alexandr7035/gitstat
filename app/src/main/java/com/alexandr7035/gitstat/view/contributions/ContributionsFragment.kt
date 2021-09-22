package com.alexandr7035.gitstat.view.contributions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.databinding.FragmentContributionsBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ContributionsFragment : Fragment() {

    private var binding: FragmentContributionsBinding? = null

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

        val days: ArrayList<ContributionDay> = arrayListOf(
            ContributionDay(date = "2020-07-17", count = 11),
            ContributionDay(date = "2020-07-18", count = 5),
            ContributionDay(date = "2020-07-19", count = 13),
            ContributionDay(date = "2020-07-20", count = 7),
            ContributionDay(date = "2020-07-21", count = 11),
            ContributionDay(date = "2020-07-22", count = 3),
            ContributionDay(date = "2020-07-23", count = 0),
            ContributionDay(date = "2020-07-24", count = 2),
            ContributionDay(date = "2020-07-25", count = 4)
        )

        val entries = ArrayList<Entry>()

//        val entries: ArrayList<Entry> = arrayListOf(
//            Entry(1f, 2f),
//            Entry(2f, 5f),
//            Entry(3f, 20f),
//            Entry(4f, 5f)
//        )

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        format.timeZone = TimeZone.getTimeZone("GMT")

        days.forEach {
            val dateStr = it.date
            val longDate = format.parse(dateStr)!!.time

            entries.add(Entry(longDate.toFloat(), it.count.toFloat()))
           //Log.d("DEBUG_TAG", longDate.toString())
        }

        val months = arrayOf("M", "T", "W", "T", "F", "S", "S")


        val dataset = LineDataSet(entries, "")
        dataset.setDrawFilled(true)
        dataset.fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.background_contributions_graph)

        val lineData = LineData(dataset)

        val vf = DateValueFormatter()

        binding!!.contributionsChart.apply {
            data = lineData
            setScaleEnabled(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            axisRight.setDrawLabels(false)

            axisRight.setDrawGridLines(false)
            axisLeft.setDrawGridLines(false)
            xAxis.setDrawGridLines(false)

            description.isEnabled = false
            data.isHighlightEnabled = false

            xAxis.valueFormatter = vf
        }
    }

    inner class DateValueFormatter: ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            //return super.getFormattedValue(value)
            return "XYU"
        }
    }

    data class ContributionDay(val date: String, val count: Int)

}