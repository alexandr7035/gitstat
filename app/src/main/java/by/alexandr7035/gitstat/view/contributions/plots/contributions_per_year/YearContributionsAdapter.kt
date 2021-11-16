package by.alexandr7035.gitstat.view.contributions.plots.contributions_per_year

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.alexandr7035.gitstat.data.local.model.ContributionsYearWithDays


class YearContributionsAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    private var items: List<ContributionsYearWithDays> = emptyList()

    fun setItems(years: List<ContributionsYearWithDays>) {
        this.items = years
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = YearContributionsFragment()

        // FIXME find better way to pass year
        val year = this.items[position].year.id

        fragment.arguments = Bundle().apply {
            putInt("year", year)
        }

        return fragment
    }

}