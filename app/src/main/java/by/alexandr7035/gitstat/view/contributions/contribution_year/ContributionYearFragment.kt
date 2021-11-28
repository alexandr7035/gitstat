package by.alexandr7035.gitstat.view.contributions.contribution_year

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.FragmentContributionYearBinding
import by.alexandr7035.gitstat.extensions.debug
import by.alexandr7035.gitstat.view.contributions.ContributionsViewModel
import by.alexandr7035.gitstat.view.repositories.RepositoriesFragmentsAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class ContributionYearFragment : Fragment() {

    private var binding: FragmentContributionYearBinding? = null
    private val viewModel by viewModels<ContributionsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentContributionYearBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val year = arguments?.getInt("year")

        Timber.debug("selected year $year")

        binding?.textVeiw?.text = year.toString()
    }
}