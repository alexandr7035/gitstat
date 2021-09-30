package com.alexandr7035.gitstat.view.contributions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alexandr7035.gitstat.databinding.ViewPlotContributionsYearBinding


class YearContributionsAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = YearCardFragment()
        return fragment
    }

}