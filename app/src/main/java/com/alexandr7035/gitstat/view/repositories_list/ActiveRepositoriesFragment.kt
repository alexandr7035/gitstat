package com.alexandr7035.gitstat.view.repositories_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.core.App
import com.alexandr7035.gitstat.core.Language
import com.alexandr7035.gitstat.databinding.FragmentActiveRepositoriesBinding
import com.alexandr7035.gitstat.view.repositories_list.filters.ReposFilters
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActiveRepositoriesFragment : Fragment() {

    private var binding: FragmentActiveRepositoriesBinding? = null

    private val viewModel by navGraphViewModels<RepositoriesListViewModel>(R.id.repositoriesListGraph) { defaultViewModelProviderFactory }

    private var filters: ReposFilters = ReposFilters()
    private var languages = emptyList<Language>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentActiveRepositoriesBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup adapter
        val adapter = RepositoriesAdapter((requireActivity().application as App).progLangManager)
        binding!!.root.adapter = adapter
        binding!!.root.layoutManager = LinearLayoutManager(context)

//        // Load filters settings for adapter from memory
//        // The default shared pref value is based on new ReposFilters() object and it's params
//        val gson = Gson()
//        val filtersStr = sharedPreferences!!.getString(getString(R.string.shared_prefs_filters), gson.toJson(ReposFilters()))
//        filters = gson.fromJson(filtersStr, ReposFilters::class.java)

//        viewModel!!.getRepositoriesData().observe(viewLifecycleOwner, { repositories ->
//
////            Update languages list on each repos list change
////            updateLanguagesList(repositories)
//
////            val filteredList = getFilteredRepositoriesList(
////                unfilteredList = repositories,
////                filters = filters
////            )
//
////            adapter.setItems(filteredList)
//            adapter.setItems(repositories)
//        })

//        binding!!.toolbar.inflateMenu(R.menu.menu_toolbar_repos_list)
//
//        binding!!.toolbar.setOnMenuItemClickListener { item ->
//            when (item.itemId) {
//                R.id.item_filters -> {
//                    showFiltersDialog()
//                }
//            }
//
//            true
//        }


        // Populate the list
//        viewModel!!.updateRepositoriesLiveData()


        viewModel.getActiveRepositoriesLiveData().observe(viewLifecycleOwner, { repos ->
            adapter.setItems(repos)
        })

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }
}