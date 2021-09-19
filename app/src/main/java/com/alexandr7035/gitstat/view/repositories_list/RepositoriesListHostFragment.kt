package com.alexandr7035.gitstat.view.repositories_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.databinding.FragmentHostRepositoriesListBinding
import com.google.android.material.tabs.TabLayoutMediator

class RepositoriesListHostFragment : Fragment() {

    private var binding: FragmentHostRepositoriesListBinding? = null

    val tabTitles = listOf(
        "Active",
        "Archived"
    )

    private val viewModel by navGraphViewModels<RepositoriesListViewModel>(R.id.repositoriesListGraph)

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

        viewModel.testScope()
    }


    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

}