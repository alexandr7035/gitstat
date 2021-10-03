package com.alexandr7035.gitstat.view.contributions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.data.local.model.ContributionsYear
import com.alexandr7035.gitstat.databinding.FragmentContributionsBinding
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.round

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

        viewModel.fetchContributionYears()

        // Update data
        viewModel.getContributionYearsLiveData().observe(viewLifecycleOwner, { years ->

            if (years.isNotEmpty()) {

                adapter = YearContributionsAdapter(this)
                adapter.setItems(years)
                binding?.yearsViewPager?.adapter = adapter
                // Set to last position
                binding?.yearsViewPager?.setCurrentItem(years.size - 1, false)

            }
        })


        viewModel.getContributionsLiveData().observe(viewLifecycleOwner, { contributions ->

            val totalContributions = contributions.sumOf { it.count }

            Log.d("DEBUG_TAG", "total $totalContributions ${contributions.size}")

            val contributionsRate = round((totalContributions.toFloat() / contributions.size.toFloat() * 100)) / 100F

            Log.d("DEBUG_TAG", "rate ${totalContributions / contributions.size}")
            Log.d("DEBUG_TAG", "rate $contributionsRate")

            binding?.totalContributions?.text = totalContributions.toString()

            binding?.contributionsRate?.text = getString(
                R.string.contributions_rate,
                contributionsRate
            )
        })
        
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