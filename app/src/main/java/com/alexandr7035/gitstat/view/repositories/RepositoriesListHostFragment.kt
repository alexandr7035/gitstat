package com.alexandr7035.gitstat.view.repositories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.databinding.FragmentHostRepositoriesListBinding
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
                Log.d("DEBUG_TAG", "reselect tab")
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

        // Update counters in the tabs
        viewModel.getActiveRepositoriesLiveData().observe(viewLifecycleOwner, {
            val repos = viewModel.getFilteredRepositoriesList(it)
            (binding!!.tabLayout.getTabAt(0) as TabLayout.Tab).text = "Active (${repos.size})"
        })

        viewModel.getArchivedRepositoriesLiveData().observe(viewLifecycleOwner, {
            val repos = viewModel.getFilteredRepositoriesList(it)
            (binding!!.tabLayout.getTabAt(1) as TabLayout.Tab).text = "Archived (${repos.size})"
        })

    }


    override fun onResume() {
        super.onResume()

        viewModel.updateActiveRepositoriesLiveData()
        viewModel.updateArchivedRepositoriesLiveData()
        viewModel.updateAllRepositoriesLiveData()
    }


    private fun showFiltersDialog() {
        Log.d("DEBUG_TAG", "show filters")
        findNavController().navigate(R.id.action_repositoriesListHostFragment_to_repositoriesFiltersDialog)
    }


    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

}