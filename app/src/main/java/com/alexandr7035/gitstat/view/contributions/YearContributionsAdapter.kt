package com.alexandr7035.gitstat.view.contributions

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alexandr7035.gitstat.data.local.model.ContributionsYear


class YearContributionsAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    private var items: List<ContributionsYear> = emptyList()

    fun setItems(years: List<ContributionsYear>) {
        this.items = years
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = YearCardFragment()

        val yearData = this.items[position]

        // FIXME may cause NPE
        fragment.arguments = Bundle().apply {
            putSerializable("yearData", yearData)
        }

        return fragment
    }

}