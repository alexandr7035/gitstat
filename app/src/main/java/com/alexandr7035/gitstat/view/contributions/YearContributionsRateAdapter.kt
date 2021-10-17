package com.alexandr7035.gitstat.view.contributions

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alexandr7035.gitstat.data.local.model.ContributionsYearWithRates

class YearContributionsRateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    // FIXME
    private var items: List<ContributionsYearWithRates> = emptyList()

    fun setItems(years: List<ContributionsYearWithRates>) {
        this.items = years
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {

        val fragment = YearContributionsRateFragment()

        val yearData = this.items[position]

        // FIXME may cause NPE
        fragment.arguments = Bundle().apply {
            putSerializable("yearData", yearData)
        }

        return fragment
    }

}