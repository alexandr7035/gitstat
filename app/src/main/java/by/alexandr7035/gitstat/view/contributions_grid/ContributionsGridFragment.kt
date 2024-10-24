package by.alexandr7035.gitstat.view.contributions_grid

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.core.extensions.navigateSafe
import by.alexandr7035.gitstat.core.extensions.observeNullSafe
import by.alexandr7035.gitstat.data.local.model.ContributionYearWithMonths
import by.alexandr7035.gitstat.data.local.model.ContributionsMonthWithDays
import by.alexandr7035.gitstat.databinding.FragmentContributionsGridBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class ContributionsGridFragment : Fragment(R.layout.fragment_contributions_grid) {
    private val binding by viewBinding(FragmentContributionsGridBinding::bind)
    private val viewModel by viewModels<ContributionsGridViewModel>()
    private val safeArgs by navArgs<ContributionsGridFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = getString(R.string.year_toolbar_title, safeArgs.contributionYear)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val adapter = MonthsAdapter(onDayClick = { contributionDay ->
            findNavController().navigateSafe(
                ContributionsGridFragmentDirections.actionContributionsGridFragmentToContributionDayDialogFragment(
                    contributionDay.count,
                    contributionDay.date,
                    contributionDay.color
                )
            )
        })

        binding.monthRecycler.adapter = adapter
        binding.monthRecycler.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getContributionYearsWithMonthsLiveData().observeNullSafe(viewLifecycleOwner) { years ->
            if (years.isNotEmpty()) {
                // Add year tabs depending on years list (reversed)
                for (year in years.reversed()) {
                    val tab = binding.tabLayout.newTab()
                    // Set year as tab text
                    tab.text = year.year.id.toString()
                    binding.tabLayout.addTab(tab)
                }


                binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab) {
                        val year = years.reversed()[tab.position]
                        adapter.setItems(getMonthsToShow(years, tab.position))
                        binding.toolbar.title = getString(R.string.year_toolbar_title, year.year.id)
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab) {

                    }

                    override fun onTabReselected(tab: TabLayout.Tab) {
                        val year = years.reversed()[tab.position]
                        adapter.setItems(getMonthsToShow(years, tab.position))
                        binding.toolbar.title = getString(R.string.year_toolbar_title, year.year.id)
                    }
                })

                // Set initial tab position
                val initialTab = binding.tabLayout.getTabAt(0)
                initialTab?.select()
            }
        }

    }


    // FIXME move to data layer
    fun getMonthsToShow(yearsWithMonths: List<ContributionYearWithMonths>, tabPosition: Int): List<ContributionsMonthWithDays> {
        val yearToDisplay = yearsWithMonths.reversed()[tabPosition]
        var monthWithDays = yearToDisplay.contributionMonths

        // Get current year
        val yearFormat = SimpleDateFormat("yyyy", Locale.US)
        val currentYear = yearFormat.format(System.currentTimeMillis()).toInt()

        // Fist contribution year
        val firstContributingYear = yearsWithMonths.first().year.id

        // Remove future month if display current year
        if (yearToDisplay.year.id == currentYear) {

            // Get current month number
            val monthFormat = SimpleDateFormat("MM", Locale.US)
            val currentMonth = monthFormat.format(System.currentTimeMillis()).toInt()

            monthWithDays = monthWithDays.slice(0 until currentMonth)
        }


        // Remove months before first contribution
        if (yearToDisplay.year.id == firstContributingYear) {
            val firstMonthIndex = monthWithDays.indexOfFirst { it -> it.contributionDays.sumOf { it.count } != 0 }

            if (firstMonthIndex != -1) {
                monthWithDays = monthWithDays.slice(firstMonthIndex until monthWithDays.size)
            }
        }

        return monthWithDays.reversed()
    }
}