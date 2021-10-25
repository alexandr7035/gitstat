package by.alexandr7035.gitstat.view.contributions

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.alexandr7035.gitstat.data.local.model.ContributionsYearWithRates

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

        // FIXME find better way to pass year
        val year = this.items[position].year.id

        fragment.arguments = Bundle().apply {
            putInt("year", year)
        }

        return fragment
    }

}