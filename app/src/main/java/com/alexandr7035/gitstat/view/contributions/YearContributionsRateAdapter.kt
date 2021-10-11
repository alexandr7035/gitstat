package com.alexandr7035.gitstat.view.contributions

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class YearContributionsRateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    // FIXME
    private var items: List<String> = emptyList()

    fun setItems(years: List<String>) {
        this.items = years
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        return YearContributionsRateFragment()
    }

}