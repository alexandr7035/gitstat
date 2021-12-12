package by.alexandr7035.gitstat.view.contributions_grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import by.alexandr7035.gitstat.data.local.model.ContributionYearWithMonths
import by.alexandr7035.gitstat.data.local.model.ContributionsMonthWithDays
import by.alexandr7035.gitstat.databinding.FragmentContributionsGridBinding
import by.alexandr7035.gitstat.core.extensions.debug
import by.alexandr7035.gitstat.core.extensions.navigateSafe
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ContributionsGridFragment : Fragment(), DayClickListener {

    private var binding: FragmentContributionsGridBinding? = null
    private val viewModel by viewModels<ContributionsGridViewModel>()
    private val safeArgs by navArgs<ContributionsGridFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentContributionsGridBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.toolbar?.title = getString(R.string.year_toolbar_title, safeArgs.contributionYear)
        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val adapter = MonthsAdapter(this)

        binding?.monthRecycler?.adapter = adapter
        binding?.monthRecycler?.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getContributionYearsWithMonthsLiveData().observe(viewLifecycleOwner, { years ->

            if (! years.isNullOrEmpty()) {

                // Add year tabs depending on years list (reversed)
                for (year in years.reversed()) {
                    val tab = binding?.tabLayout?.newTab()
                    // Set year as tab text
                    tab?.text = year.year.id.toString()

                    if (tab != null) {
                        binding?.tabLayout?.addTab(tab)
                    }
                }


                binding?.tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab) {
                        val year = years.reversed()[tab.position]
                        adapter.setItems(getMonthsToShow(years, tab.position))
                        binding?.toolbar?.title = getString(R.string.year_toolbar_title, year.year.id)
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab) {

                    }

                    override fun onTabReselected(tab: TabLayout.Tab) {
                        val year = years.reversed()[tab.position]
                        adapter.setItems(getMonthsToShow(years, tab.position))
                        binding?.toolbar?.title = getString(R.string.year_toolbar_title, year.year.id)
                    }
                })

                // Set initial tab position
                val initialTab = binding?.tabLayout?.getTabAt(0)
                initialTab?.select()

            }

        })

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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    // Handle contribution cells clicks here
    override fun onDayItemClick(contributionDay: ContributionDayEntity) {
        Timber.debug("click in FRAGMENT $contributionDay")

        findNavController().navigateSafe(ContributionsGridFragmentDirections.actionContributionsGridFragmentToContributionDayDialogFragment(
            contributionDay.count,
            contributionDay.date,
            contributionDay.color
        ))
    }
}