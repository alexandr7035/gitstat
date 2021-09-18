package com.alexandr7035.gitstat.view.repositories_list

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class RepositoriesFragmentsAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {

        val fragment = when (position) {
            0 -> ActiveRepositoriesFragment()
            else -> ArchivedRepositoriesFragment()
        }

        return fragment
    }
}