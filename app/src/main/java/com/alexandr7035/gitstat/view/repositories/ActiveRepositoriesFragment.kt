package com.alexandr7035.gitstat.view.repositories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.core.App
import com.alexandr7035.gitstat.databinding.FragmentActiveRepositoriesBinding
import com.alexandr7035.gitstat.view.repositories.filters.RepositoriesListFiltersHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActiveRepositoriesFragment : Fragment() {

    private val viewModel by navGraphViewModels<RepositoriesViewModel>(R.id.repositoriesListGraph) { defaultViewModelProviderFactory }
    private var binding: FragmentActiveRepositoriesBinding? = null
    private var adapter: RepositoriesAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentActiveRepositoriesBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup adapter
        adapter = RepositoriesAdapter()
        binding!!.root.adapter = adapter
        binding!!.root.layoutManager = LinearLayoutManager(context)

        viewModel.getTabRefreshedLiveData().observe(viewLifecycleOwner, {
            // If current fragment
            // FIXME find better solution
            if (it == 0) {
                //binding!!.root.smoothScrollToPosition(0)
                binding!!.root.layoutManager!!.scrollToPosition(0)
            }
        })
    }

    override fun onResume() {
        super.onResume()

        viewModel.getActiveRepositoriesLiveData().observe(viewLifecycleOwner, { repos ->
            val filteredList = RepositoriesListFiltersHelper.getFilteredRepositoriesList(
                repos,
                viewModel.getRepositoriesFilters()
            )
            adapter!!.setItems(filteredList)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }
}