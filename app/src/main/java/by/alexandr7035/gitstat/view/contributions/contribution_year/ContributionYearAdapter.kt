package by.alexandr7035.gitstat.view.contributions.contribution_year

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.alexandr7035.gitstat.data.local.model.ContributionsYearWithDays

class ContributionYearAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    private var items: List<ContributionsYearWithDays> = emptyList()

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(years: List<ContributionsYearWithDays>) {
        this.items = years
        notifyDataSetChanged()
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = ContributionYearFragment()

        val year = this.items[position].year.id

        fragment.arguments = Bundle().apply {
            putInt("year", year)
        }

        return fragment
    }
}