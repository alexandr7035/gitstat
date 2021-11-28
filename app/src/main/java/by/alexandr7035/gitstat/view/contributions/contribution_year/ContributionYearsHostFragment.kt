package by.alexandr7035.gitstat.view.contributions.contribution_year

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.alexandr7035.gitstat.databinding.FragmentContributionYearsHostBinding
import by.alexandr7035.gitstat.view.contributions.ContributionsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ContributionYearsHostFragment : Fragment() {

    private var binding: FragmentContributionYearsHostBinding? = null
    private val viewModel by viewModels<ContributionsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentContributionYearsHostBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Setup viewpager and tab layout
        val pagerAdapter = ContributionYearAdapter(this)
        binding!!.yearsPager.adapter = pagerAdapter


        viewModel.getContributionYearsWithDaysLiveData().observe(viewLifecycleOwner, { years ->

            if (!years.isNullOrEmpty()) {

                pagerAdapter.setItems(years.reversed())

                TabLayoutMediator(binding!!.tabLayout, binding!!.yearsPager) { tab, position ->
                    tab.text = years.reversed()[position].year.id.toString()
                }.attach()

            }

        })
    }
}