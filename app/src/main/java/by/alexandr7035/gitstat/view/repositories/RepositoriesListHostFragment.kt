package by.alexandr7035.gitstat.view.repositories

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.FragmentHostRepositoriesListBinding
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoriesListHostFragment : Fragment() {

    private var binding: FragmentHostRepositoriesListBinding? = null

    private val tabTitles = listOf(
        "Active",
        "Archived"
    )

    private val viewModel by navGraphViewModels<RepositoriesViewModel>(R.id.repositoriesListGraph) { defaultViewModelProviderFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHostRepositoriesListBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val pagerAdapter = RepositoriesFragmentsAdapter(this)
        binding!!.pager.adapter = pagerAdapter

        TabLayoutMediator(binding!!.tabLayout, binding!!.pager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        binding!!.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                viewModel.refreshTabRecycler(tab.position)
            }

        })

        // Inflate toolbar menu
        binding!!.toolbar.inflateMenu(R.menu.menu_toolbar_repos_list)
        binding!!.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item_filters -> {
                    showFiltersDialog()
                }
            }

            true
        }

        // Icon mark for applied filters
        val badge: BadgeDrawable = BadgeDrawable.create(requireContext())
        badge.backgroundColor = ContextCompat.getColor(requireContext(), R.color.yellow_400)
        BadgeUtils.attachBadgeDrawable(badge, binding!!.toolbar, R.id.item_filters)

        // Update counters in the tabs
        viewModel.getActiveRepositoriesLiveData().observe(viewLifecycleOwner, {
            val repos = viewModel.getFilteredRepositoriesList(it)
            (binding!!.tabLayout.getTabAt(0) as TabLayout.Tab).text = "Active (${repos.size})"
        })

        viewModel.getArchivedRepositoriesLiveData().observe(viewLifecycleOwner, {
            val repos = viewModel.getFilteredRepositoriesList(it)
            (binding!!.tabLayout.getTabAt(1) as TabLayout.Tab).text = "Archived (${repos.size})"
        })

        viewModel.getAllRepositoriesListLiveData().observe(viewLifecycleOwner, { unfilteredRepos ->

            val filteredRepos = viewModel.getFilteredRepositoriesList(unfilteredRepos)

            // When unfiltered and filtered lists differ in length
            // Means some filters are applied
            // Show or hide badge
            badge.isVisible = unfilteredRepos.size != filteredRepos.size

            // Disable filters when no repos at all
            binding?.toolbar?.menu?.findItem(R.id.item_filters)?.isEnabled = unfilteredRepos.isNotEmpty()
        })
    }


    override fun onResume() {
        super.onResume()

        viewModel.updateActiveRepositoriesLiveData()
        viewModel.updateArchivedRepositoriesLiveData()
        viewModel.updateAllRepositoriesLiveData()
    }


    private fun showFiltersDialog() {
        findNavController().navigate(R.id.action_repositoriesListHostFragment_to_repositoriesFiltersDialog)
    }


    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

}