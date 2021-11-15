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
import by.alexandr7035.gitstat.databinding.FragmentContributionsGridBinding
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContributionsGridFragment : Fragment() {

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

        val adapter = MonthsAdapter()

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
                        adapter.setItems(years.reversed()[tab.position].contributionMonths)
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab) {

                    }

                    override fun onTabReselected(tab: TabLayout.Tab) {
                        adapter.setItems(years.reversed()[tab.position].contributionMonths)
                    }
                })

                // Set initial tab position
                val initialTab = binding?.tabLayout?.getTabAt(0)
                initialTab?.select()

            }

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}