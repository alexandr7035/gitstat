package com.alexandr7035.gitstat.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.alexandr7035.gitstat.databinding.FragmentReposBinding
import com.alexandr7035.gitstat.databinding.FragmentRepositoriesListBinding
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader


class RepositoriesListFragment : Fragment() {

    private var binding: FragmentRepositoriesListBinding? = null
    private var sharedPreferences: SharedPreferences? = null
    private var viewModel: MainViewModel? = null

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

        val adapter = RepositoriesAdapter(languagesColorsList)
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel!!.getRepositoriesData().observe(viewLifecycleOwner, {
            adapter.setItems(it)
        })

        viewModel!!.updateRepositoriesLiveData()


        binding!!.toolbar.inflateMenu(R.menu.menu_toolbar_repos_list)

        binding!!.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item_filters -> {
                    Log.d("DEBUG", "filters clicked")
                }
            }

            true
        }
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

}