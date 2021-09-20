package com.alexandr7035.gitstat.view.repositories_list

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.core.App
import com.alexandr7035.gitstat.core.Language
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.alexandr7035.gitstat.databinding.FragmentRepositoriesListBinding
import com.alexandr7035.gitstat.view.MainViewModel
import com.alexandr7035.gitstat.view.repositories_list.filters.ReposFilters
import com.alexandr7035.gitstat.view.repositories_list.filters.RepositoriesFiltersDialog
import com.alexandr7035.gitstat.view.repositories_list.filters.RepositoriesSorter
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoriesListFragment : Fragment(), RepositoriesFiltersDialog.FiltersUpdateObserver {

    private var binding: FragmentRepositoriesListBinding? = null
    private var sharedPreferences: SharedPreferences? = null
    private var viewModel: MainViewModel? = null

    private var filters: ReposFilters = ReposFilters()
    private var languages = emptyList<Language>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRepositoriesListBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Shared pref
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val token = sharedPreferences!!.getString(getString(R.string.shared_pref_token), "NONE")

        //viewModel = MainViewModel(requireActivity().application,  "$token")

        binding!!.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Setup adapter
        val adapter = RepositoriesAdapter((requireActivity().application as App).progLangManager)
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.layoutManager = LinearLayoutManager(context)

        // Load filters settings for adapter from memory
        // The default shared pref value is based on new ReposFilters() object and it's params
        val gson = Gson()
        val filtersStr = sharedPreferences!!.getString(getString(R.string.shared_prefs_filters), gson.toJson(ReposFilters()))
        filters = gson.fromJson(filtersStr, ReposFilters::class.java)

        viewModel!!.getRepositoriesData().observe(viewLifecycleOwner, { repositories ->

            // Update languages list on each repos list change
            updateLanguagesList(repositories)

            val filteredList = getFilteredRepositoriesList(
                unfilteredList = repositories,
                filters = filters
            )

            adapter.setItems(filteredList)
        })

        binding!!.toolbar.inflateMenu(R.menu.menu_toolbar_repos_list)

        binding!!.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item_filters -> {
                    showFiltersDialog()
                }
            }

            true
        }


        // Populate the list
        viewModel!!.updateRepositoriesLiveData()
    }


    private fun showFiltersDialog() {
        val dialog = RepositoriesFiltersDialog(
            currentFilters = filters,
            filtersUpdateObserver = this,
            languages = languages
        )
        dialog.show(requireActivity().supportFragmentManager, "filtersDialog")
    }


    private fun getFilteredRepositoriesList(unfilteredList: List<RepositoryEntity>, filters: ReposFilters): List<RepositoryEntity> {
        //Log.d("DEBUG_TAG", "apply $filters")

        val filteredList = ArrayList<RepositoryEntity>()

        // Filtering
        // Private / public repos
        when (filters.filterPrivacy) {
            ReposFilters.FilterPrivacy.PUBLIC_REPOS_ONLY -> unfilteredList.forEach {
                if (! it.isPrivate) {
                    filteredList.add(it)
                }
            }

            ReposFilters.FilterPrivacy.PRIVATE_REPOS_ONLY -> unfilteredList.forEach {
                if (it.isPrivate) {
                    filteredList.add(it)
                }
            }

            else -> filteredList.addAll(unfilteredList)
        }

        // Forks
        when (filters.filterForks) {
            ReposFilters.FilterForks.FORKS_ONLY -> unfilteredList.forEach {
                if (! it.fork) {
                    filteredList.remove(it)
                }
            }

            ReposFilters.FilterForks.EXCLUDE_FORKS -> unfilteredList.forEach {
                if (it.fork) {
                    filteredList.remove(it)
                }
            }

            ReposFilters.FilterForks.ALL_REPOS -> true
        }

        // Remove all repos if language is not from filters' set
        if (filters.filterLanguages.isNotEmpty()) {
            filteredList.removeAll {
                !filters.filterLanguages.contains(it.language)
            }
        }

        // Sorting
        when (filters.sortingType) {
            ReposFilters.SortingType.BY_REPO_NAME -> {
                when (filters.sortingOrder) {
                    ReposFilters.SortingOrder.ASCENDING_MODE -> RepositoriesSorter.sortByRepoNameAscending(filteredList)
                    ReposFilters.SortingOrder.DESCENDING_MODE -> RepositoriesSorter.sortByRepoNameDescending(filteredList)
                }
            }

            ReposFilters.SortingType.BY_REPO_CREATION_DATE -> {
                when (filters.sortingOrder) {
                    ReposFilters.SortingOrder.ASCENDING_MODE -> RepositoriesSorter.sortByRepoCreationDateAscending(filteredList)
                    ReposFilters.SortingOrder.DESCENDING_MODE -> RepositoriesSorter.sortByRepoCreationDateDescending(filteredList)
                }
            }
        }

        // TODO add logics for filters mark on toolbar here
        // TODO update screenshots in README

        return filteredList
    }


    // Called when "apply" button is clicked in filters dialog
    override fun onFiltersUpdated(filters: ReposFilters) {
        //Log.d("DEBUG", "update filters $filters")
        this.filters = filters

        // FIXME
        viewModel!!.updateRepositoriesLiveData()

        // Save changes in memory
        val gson = Gson()
        sharedPreferences!!.edit()
            .putString(getString(R.string.shared_prefs_filters), gson.toJson(filters))
            .apply()
    }


    private fun updateLanguagesList(repositories: List<RepositoryEntity>) {
        languages = (requireActivity().application as App).progLangManager.getLanguagesList(repositories)
    }

}