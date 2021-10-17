package com.alexandr7035.gitstat.view.repositories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.databinding.FragmentReposOverviewBinding
import com.alexandr7035.gitstat.view.repositories.plots.languages_plot.LanguagesPlot
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReposOverviewFragment : Fragment() {

    private var binding: FragmentReposOverviewBinding? = null
    private val viewModel by viewModels<RepositoriesViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentReposOverviewBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val plot = LanguagesPlot()
        // Setup chart configuration
        if (binding?.languagesChart != null) {
            plot.setupPlot(binding!!.languagesChart)
        }

        viewModel.getAllRepositoriesListLiveData().observe(viewLifecycleOwner, { repos ->

            // Populate chart with data
            plot.setLanguagesData(
                chart = binding!!.languagesChart,
                languages = viewModel.getLanguagesForReposList(repos),
                totalReposCount = repos.size
            )

            binding!!.totalReposCountView.text = repos.size.toString()
            binding!!.privateReposCountView.text = repos.filter { it.isPrivate }.size.toString()
            binding!!.publicReposCountView.text = repos.filter { ! it.isPrivate }.size.toString()

        })


        binding!!.toReposListBtn.setOnClickListener {
            findNavController().navigate(R.id.action_reposFragment_to_repositoriesListHostFragment)
        }

        // FIXME find better solution (see viewmodel)
        viewModel.updateAllRepositoriesLiveData()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}