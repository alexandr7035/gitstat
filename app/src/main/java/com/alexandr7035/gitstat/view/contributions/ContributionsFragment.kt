package com.alexandr7035.gitstat.view.contributions

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.databinding.FragmentContributionsBinding
import com.alexandr7035.gitstat.view.contributions.plots.contributions_ratio.ContributionsRatioPlot
import com.alexandr7035.gitstat.view.contributions.plots.contributions_ratio.RatioLegendAdapter
import com.alexandr7035.gitstat.view.contributions.plots.contributions_ratio.RatioLegendItem
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.round

@AndroidEntryPoint
class ContributionsFragment : Fragment() {

    private var binding: FragmentContributionsBinding? = null
    private val viewModel by viewModels<ContributionsViewModel>()

    private lateinit var yearContributionsAdapter: YearContributionsAdapter
    private lateinit var yearContributionsRateAdapter: YearContributionsRateAdapter

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

        // Update data
        viewModel.getContributionYearsLiveData().observe(viewLifecycleOwner, { years ->

            if (years.isNotEmpty()) {
                yearContributionsAdapter = YearContributionsAdapter(this)
                yearContributionsAdapter.setItems(years)
                binding?.yearsViewPager?.adapter = yearContributionsAdapter

                // Set to last position
                binding?.yearsViewPager?.setCurrentItem(years.size - 1, false)
                binding?.currentYearView?.text = years[years.size-1].year.id.toString()

                // Change year in card title when viewpager item changes
                binding?.yearsViewPager?.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        Log.d("DEBUG_TAG", "Page changed callback")
                        binding?.currentYearView?.text = years[position].year.id.toString()
                    }
                })

                // Attach tablayout
                TabLayoutMediator(binding!!.yearsTabLayout, binding!!.yearsViewPager) {
                        tab, position ->
                }.attach()

            }
        })


        viewModel.getContributionDaysLiveData().observe(viewLifecycleOwner, { contributions ->

            val totalContributions = contributions.sumOf { it.count }

            Log.d("DEBUG_TAG", "total $totalContributions ${contributions.size}")

            val contributionsRate = round((totalContributions.toFloat() / contributions.size.toFloat() * 100)) / 100F

            Log.d("DEBUG_TAG", "rate ${totalContributions / contributions.size}")
            Log.d("DEBUG_TAG", "rate $contributionsRate")

            binding?.totalContributions?.text = totalContributions.toString()

            binding?.contributionsRate?.text = contributionsRate.toString()
        })


        viewModel.getContributionYearsWithRatesLiveData().observe(viewLifecycleOwner, { rateYears ->

            Log.d("DEBUG_TAG", "rate years count ${rateYears.size}")

            if (rateYears.isNotEmpty()) {
                yearContributionsRateAdapter  = YearContributionsRateAdapter(this)
                yearContributionsRateAdapter.setItems(rateYears)
                binding?.rateViewPager?.adapter = yearContributionsRateAdapter

                TabLayoutMediator(binding!!.rateTabLayout, binding!!.rateViewPager) { tab, position ->
                }.attach()

                // Set to last position
                binding?.rateViewPager?.setCurrentItem(rateYears.size - 1, false)
            }
        })


        viewModel.getContributionsRatioLiveData().observe(viewLifecycleOwner, { ratios ->
            Log.d("DEBUG_TAG", "ratios ${ratios}")

            // FIXME
            val adapter = RatioLegendAdapter()
            binding?.ratioLegendRecycler?.layoutManager = LinearLayoutManager(requireContext())
            binding?.ratioLegendRecycler?.adapter = adapter

            if (binding?.ratioChart != null) {
                val plot = ContributionsRatioPlot()
                plot.setupPlot(binding!!.ratioChart, ratios)
                adapter.setItems(plot.getRatioLegendItems(binding!!.ratioChart, ratios))
            }
        })
    }

}