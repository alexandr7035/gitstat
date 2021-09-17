package com.alexandr7035.gitstat.view.repositories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.databinding.FragmentBaseRepositoriesListBinding
import com.google.android.material.tabs.TabLayoutMediator

class FragmentBaseRepositoriesList : Fragment() {

    private var binding: FragmentBaseRepositoriesListBinding? = null

    private val PAGES_COUNT = 3

    val tabTitles = listOf(
        getString(R.string.active),
        getString(R.string.archived)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentBaseRepositoriesListBinding.inflate(inflater, container, false)
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
    }

}