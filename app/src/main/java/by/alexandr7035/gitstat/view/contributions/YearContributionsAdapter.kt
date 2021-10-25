package by.alexandr7035.gitstat.view.contributions

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.alexandr7035.gitstat.data.local.model.ContributionsYearWithDays


class YearContributionsAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    private var items: List<ContributionsYearWithDays> = emptyList()

    fun setItems(years: List<ContributionsYearWithDays>) {
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