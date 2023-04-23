package by.alexandr7035.gitstat.view.repositories.repo_list

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.alexandr7035.gitstat.view.repositories.repo_list.ActiveRepositoriesFragment
import by.alexandr7035.gitstat.view.repositories.repo_list.ArchivedRepositoriesFragment

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