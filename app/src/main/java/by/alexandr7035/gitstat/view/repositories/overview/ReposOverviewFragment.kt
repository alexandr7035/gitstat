package by.alexandr7035.gitstat.view.repositories.overview

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Orientation
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.core.extensions.navigateSafe
import by.alexandr7035.gitstat.core.extensions.observeNullSafe
import by.alexandr7035.gitstat.databinding.FragmentReposOverviewBinding
import by.alexandr7035.gitstat.view.MainActivity
import by.alexandr7035.gitstat.view.repositories.RepositoriesViewModel
import by.alexandr7035.gitstat.view.repositories.overview.scrollable_repos_bar.ReposBarItems
import by.alexandr7035.gitstat.view.repositories.overview.scrollable_repos_bar.ReposScrollableBarAdapter
import by.alexandr7035.gitstat.view.repositories.plots.languages_plot.LanguagesPlot
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ReposOverviewFragment : Fragment(R.layout.fragment_repos_overview) {

    private val binding by viewBinding(FragmentReposOverviewBinding::bind)
    private val viewModel by viewModels<RepositoriesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val plot = LanguagesPlot()
        // Setup chart configuration
        plot.setupPlot(binding.languagesChart)

        viewModel.getRepositoriesLiveData().observeNullSafe(viewLifecycleOwner) { repos ->

            Timber.tag("DEBUG_TAG").d("repos $repos")

            binding.totalReposCountView.text = repos.size.toString()
            binding.privateReposCountView.text = repos.filter { it.isPrivate }.size.toString()
            binding.publicReposCountView.text = repos.filterNot { it.isPrivate }.size.toString()

            // Show stub instead of plot if list is empty
            if (repos.isEmpty()) {
                binding.languagesChart.visibility = View.GONE
                binding.noReposStub.visibility = View.VISIBLE
            } else {
                binding.languagesChart.visibility = View.VISIBLE
                binding.noReposStub.visibility = View.GONE

                // Populate chart with data
                plot.setLanguagesData(
                    chart = binding.languagesChart, languages = viewModel.getLanguagesForReposList(repos), totalReposCount = repos.size
                )

                binding.reposBar.isVisible = true

                val pinnedRepos = repos.filter {
                    it.isPinned
                }.map {
                    ReposBarItems.PinnedRepo(
                        repoName = it.name
                    )
                }

                val mostStarredRepo = repos.maxBy { it.stars }
                val items = listOf<ReposBarItems>(
                    ReposBarItems.MetricCard(
                        repoName = mostStarredRepo.name,
                        metricName = "Most stars",
                        metricValue = mostStarredRepo.stars,
                        // FIXME
                        iconResId = 0
                    ),
                ) + pinnedRepos
                binding.reposBar.adapter = ReposScrollableBarAdapter(items)
                binding.reposBar.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            }
        }


        binding.toReposListBtn.setOnClickListener {
            findNavController().navigateSafe(ReposOverviewFragmentDirections.actionReposFragmentToRepositoriesListHostFragment())
        }

        binding.drawerBtn.setOnClickListener {
            (requireActivity() as MainActivity).openDrawerMenu()
        }

    }
}