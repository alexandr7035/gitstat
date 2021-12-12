package by.alexandr7035.gitstat.view.contributions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.FragmentContributionsBinding
import by.alexandr7035.gitstat.core.extensions.navigateSafe
import by.alexandr7035.gitstat.core.extensions.observeNullSafe
import by.alexandr7035.gitstat.view.MainActivity
import by.alexandr7035.gitstat.view.contributions.plots.contributions_per_year.YearContributionsAdapter
import by.alexandr7035.gitstat.view.contributions.plots.contributions_rate.YearContributionRatesAdapter
import by.alexandr7035.gitstat.view.contributions.plots.contributions_types.TypesAdapter
import by.alexandr7035.gitstat.view.contributions.plots.contributions_types.ContributionTypesListToRecyclerItemsMapper
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ContributionsFragment : Fragment() {

    private var binding: FragmentContributionsBinding? = null
    private val viewModel by viewModels<ContributionsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentContributionsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter for contributions count plot
        val yearContributionsAdapter = YearContributionsAdapter(this)
        binding?.yearsViewPager?.adapter = yearContributionsAdapter

        // Adapter for contribution rate plot
        val yearContributionsRateAdapter = YearContributionRatesAdapter(this)
        binding?.rateViewPager?.adapter = yearContributionsRateAdapter

        // Adapter for legend on contribution types plot
        val typesLegendAdapter = TypesAdapter()
        binding?.contributionTypesRecycler?.layoutManager = LinearLayoutManager(requireContext())
        binding?.contributionTypesRecycler?.adapter = typesLegendAdapter

        // Update data
        viewModel.getContributionYearsLiveData().observeNullSafe(viewLifecycleOwner, { years ->
            if (years.isNotEmpty()) {

                yearContributionsAdapter.setItems(years)

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
        })


        viewModel.getContributionDaysLiveData().observeNullSafe(viewLifecycleOwner, { contributions ->
            val totalContributions = contributions.sumOf { it.count }
            binding?.totalContributions?.text = totalContributions.toString()
        })


        viewModel.getContributionYearsWithRatesLiveData().observeNullSafe(viewLifecycleOwner, { rateYears ->
            if (rateYears.isNotEmpty()) {
                yearContributionsRateAdapter.setItems(rateYears)

                TabLayoutMediator(binding!!.rateTabLayout, binding!!.rateViewPager) { tab, position ->
                }.attach()

                // Set to last position
                binding?.rateViewPager?.setCurrentItem(rateYears.size - 1, false)

                // Set total contribution rate in header
                binding?.contributionsRate?.text =
                    viewModel.getLastTotalContributionRateForYear(rateYears[rateYears.size - 1]).toString()
            }
        })




        viewModel.getContributionTypesLiveData().observeNullSafe(viewLifecycleOwner, { typesData ->
            // Update legend
            binding?.contributionTypesRecycler?.suppressLayout(false)
            val types = ContributionTypesListToRecyclerItemsMapper.map(typesData, requireContext())
            typesLegendAdapter.setItems(types)
            binding?.contributionTypesRecycler?.suppressLayout(true)

            // Setup ratio bar bar
            val values = ArrayList<Float>()
            val colors = ArrayList<Int>()

            for (type in types) {
                colors.add(type.color)
                values.add(type.count.toFloat())
            }

            binding?.typesRatioView?.setValues(values, colors)
            binding?.typesRatioView?.invalidate()
        })

        binding?.drawerBtn?.setOnClickListener {
            (requireActivity() as MainActivity).openDrawerMenu()
        }

        // Help icon for contribution rate
        binding?.contributionRateHelpIcon?.setOnClickListener {
            findNavController().navigateSafe(
                ContributionsFragmentDirections.actionGlobalInfoDialogFragment(
                    getString(R.string.what_is_contribution_rate_title),
                    getString(R.string.what_is_contribution_rate_text)
                )
            )
        }

        // Help icon for contribution rate
        binding?.contributionRateDynamicsHelpIcon?.setOnClickListener {
            findNavController().navigateSafe(
                ContributionsFragmentDirections.actionGlobalInfoDialogFragment(
                    getString(R.string.contribution_rate_dynamics_help_title),
                    getString(R.string.contribution_rate_dynamics_help_text)
                )
            )
        }


        binding?.toContributionsGridBtn?.setOnClickListener {
            findNavController().navigateSafe(ContributionsFragmentDirections.actionContributionsFragmentToContributionsGridFragment(2021))
        }

    }

}