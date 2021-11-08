package by.alexandr7035.gitstat.view.contributions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.FragmentContributionsBinding
import by.alexandr7035.gitstat.view.MainActivity
import by.alexandr7035.gitstat.view.contributions.plots.contributions_per_year.YearContributionsAdapter
import by.alexandr7035.gitstat.view.contributions.plots.contributions_rate.YearContributionRatesAdapter
import by.alexandr7035.gitstat.view.contributions.plots.contributions_types.ContributionTypesPlot
import by.alexandr7035.gitstat.view.contributions.plots.contributions_types.TypesLegendAdapter
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ContributionsFragment : Fragment() {

    private var binding: FragmentContributionsBinding? = null
    private val viewModel by viewModels<ContributionsViewModel>()

    private lateinit var yearContributionsAdapter: YearContributionsAdapter
    private lateinit var yearContributionsRateAdapter: YearContributionRatesAdapter

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

            if (years != null) {

                if (years.isNotEmpty()) {
                    yearContributionsAdapter = YearContributionsAdapter(this)
                    yearContributionsAdapter.setItems(years)
                    binding?.yearsViewPager?.adapter = yearContributionsAdapter

                    // Set to last position
                    binding?.yearsViewPager?.setCurrentItem(years.size - 1, false)
                    binding?.currentYearView?.text = years[years.size - 1].year.id.toString()

                    // Change year in card title when viewpager item changes
                    binding?.yearsViewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            Timber.d("Page changed callback")
                            binding?.currentYearView?.text = years[position].year.id.toString()
                        }
                    })

                    // Attach tablayout
                    TabLayoutMediator(binding!!.yearsTabLayout, binding!!.yearsViewPager) { tab, position ->
                    }.attach()

                }
            }
        })


        viewModel.getContributionDaysLiveData().observe(viewLifecycleOwner, { contributions ->
            if (contributions != null) {
                val totalContributions = contributions.sumOf { it.count }
                binding?.totalContributions?.text = totalContributions.toString()
            }
        })


        viewModel.getContributionYearsWithRatesLiveData().observe(viewLifecycleOwner, { rateYears ->

            if (rateYears != null) {

                if (rateYears.isNotEmpty()) {
                    yearContributionsRateAdapter = YearContributionRatesAdapter(this)
                    yearContributionsRateAdapter.setItems(rateYears)
                    binding?.rateViewPager?.adapter = yearContributionsRateAdapter

                    TabLayoutMediator(binding!!.rateTabLayout, binding!!.rateViewPager) { tab, position ->
                    }.attach()

                    // Set to last position
                    binding?.rateViewPager?.setCurrentItem(rateYears.size - 1, false)

                    // Set total contribution rate in header
                    binding?.contributionsRate?.text =
                        viewModel.getLastTotalContributionRateForYear(rateYears[rateYears.size - 1]).toString()
                }
            }
        })


        viewModel.getContributionTypesLiveData().observe(viewLifecycleOwner, { types ->

            if (types != null) {

                // FIXME
                val adapter = TypesLegendAdapter()
                binding?.contributionTypesLegendRecycler?.layoutManager = FlexboxLayoutManager(requireContext())
                binding?.contributionTypesLegendRecycler?.adapter = adapter

                if (binding?.contributionTypesChart != null) {
                    val plot = ContributionTypesPlot()
                    plot.setupPlot(binding!!.contributionTypesChart, types)
                    adapter.setItems(plot.getContributionTypesLegendItems(binding!!.contributionTypesChart, types))
                }
            }
        })

        binding?.drawerBtn?.setOnClickListener {
            (requireActivity() as MainActivity).openDrawerMenu()
        }

        // Help icon for contribution rate
        binding?.contributionRateHelpIcon?.setOnClickListener {
            findNavController().navigate(ContributionsFragmentDirections.actionGlobalInfoDialogFragment(
                getString(R.string.what_is_contribution_rate_title),
                getString(R.string.what_is_contribution_rate_text)
            ))
        }

        // Help icon for contribution rate
        binding?.contributionRateDynamicsHelpIcon?.setOnClickListener {
            findNavController().navigate(ContributionsFragmentDirections.actionGlobalInfoDialogFragment(
                getString(R.string.contribution_rate_dynamics_help_title),
                getString(R.string.contribution_rate_dynamics_help_text)
            ))
        }
    }

}