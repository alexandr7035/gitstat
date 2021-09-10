package com.alexandr7035.gitstat.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.alexandr7035.gitstat.databinding.FragmentRepositoriesListBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader


class RepositoriesListFragment : Fragment(), RepositoriesFiltersDialog.FiltersUpdateObserver {

    private var binding: FragmentRepositoriesListBinding? = null
    private var sharedPreferences: SharedPreferences? = null
    private var viewModel: MainViewModel? = null

    // FIXME
    private var filters: ReposFilters = ReposFilters()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRepositoriesListBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Shared pref
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val user = sharedPreferences!!.getString(getString(R.string.shared_pref_login), "NONE")!!
        val token = sharedPreferences!!.getString(getString(R.string.shared_pref_token), "NONE")

        viewModel = MainViewModel(requireActivity().application, "$user", "$token")

        binding!!.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Load lang colors
        // FIXME move to other place
        val languagesColorsList: Map<String, Map<String, String>> = getLangColorsList()

        // Setup adapter
        val adapter = RepositoriesAdapter(languagesColorsList)
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.layoutManager = LinearLayoutManager(context)

        // Load filters settings for adapter from memory
        // The default shared pref value is based on new ReposFilters() object and it's params
        val gson = Gson()
        val filtersStr = sharedPreferences!!.getString(getString(R.string.shared_prefs_filters), gson.toJson(ReposFilters()))
        filters = gson.fromJson(filtersStr, ReposFilters::class.java)

        viewModel!!.getRepositoriesData().observe(viewLifecycleOwner, { repositories ->

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
            filtersUpdateObserver = this
        )
        dialog.show(requireActivity().supportFragmentManager, "filtersDialog")
    }


    // FIXME
    private fun getLangColorsList(): Map<String, Map<String, String>> {
        // Read colors jsom from resources
        val inputStream = resources.openRawResource(R.raw.language_colors)
        val reader = InputStreamReader(inputStream)
        val builder = GsonBuilder()
        val itemsMapType = object : TypeToken<Map<String, Map<String, String>>>() {}.type

        return builder.create().fromJson(reader, itemsMapType)
    }


    private fun getFilteredRepositoriesList(unfilteredList: List<RepositoryEntity>, filters: ReposFilters): List<RepositoryEntity> {
        val filteredList = ArrayList<RepositoryEntity>()

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

        return filteredList
    }


    // Called when "apply" button is clicked in filters dialog
    override fun onFiltersUpdated(filters: ReposFilters) {
        Log.d("DEBUG", "update filters $filters")
        this.filters = filters

        // FIXME
        viewModel!!.updateRepositoriesLiveData()

        // Save changes in memory
        val gson = Gson()
        sharedPreferences!!.edit()
            .putString(getString(R.string.shared_prefs_filters), gson.toJson(filters))
            .apply()
    }

}