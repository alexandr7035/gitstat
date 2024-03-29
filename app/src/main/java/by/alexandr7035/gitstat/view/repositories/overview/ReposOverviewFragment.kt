package by.alexandr7035.gitstat.view.repositories.overview

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.core.extensions.navigateSafe
import by.alexandr7035.gitstat.core.extensions.observeNullSafe
import by.alexandr7035.gitstat.databinding.FragmentReposOverviewBinding
import by.alexandr7035.gitstat.view.MainActivity
import by.alexandr7035.gitstat.view.core.BRAND_COLORS
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

        // Pinned repos
        val reposAdapter = ReposScrollableBarAdapter(repoClickCallback = { repoId ->
            findNavController().navigateSafe(
                ReposOverviewFragmentDirections
                    .actionReposOverviewFragmentToRepositoryPageFragment(repoId)
            )
        })
        binding.reposBar.adapter = reposAdapter
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        val decoration = DividerItemDecoration(
            binding.reposBar.context,
            layoutManager.orientation
        )

        ContextCompat.getDrawable(requireContext(), R.drawable.decoration_repository_item_horizontal)?.let {
            decoration.setDrawable(it)
        }

        binding.reposBar.addItemDecoration(decoration)
        binding.reposBar.layoutManager = layoutManager

        viewModel.getRepositoriesLiveData().observeNullSafe(viewLifecycleOwner) { repos ->
            binding.totalReposCountView.text = repos.size.toString()
            binding.privateReposCountView.text = repos.filter { it.isPrivate }.size.toString()
            binding.publicReposCountView.text = repos.filterNot { it.isPrivate }.size.toString()

            // Show stub instead of plot if list is empty
            if (repos.isEmpty()) {
                binding.languagesChart.isVisible = false
                binding.reposBar.isVisible = false
                binding.noReposStub.isVisible = true
            } else {
                binding.languagesChart.isVisible = true
                binding.noReposStub.isVisible = false

                // Populate chart with data
                plot.setLanguagesData(
                    chart = binding.languagesChart, languages = viewModel.getLanguagesForReposList(repos), totalReposCount = repos.size
                )

                val pinnedRepos = repos.filter {
                    it.isPinned
                }.sortedByDescending { it.stars }
                    .mapIndexed { index, it ->
                        val color = BRAND_COLORS[index % BRAND_COLORS.size] // Choose a color based on the current index
                        ReposBarItems.PinnedRepo(
                            repoId = it.id,
                            repoName = it.name,
                            repoLang = it.primaryLanguage,
                            repoLangColor = it.primaryLanguageColor,
                            bgColorRes = color,
                            stars = it.stars,
                        )
                    }

                if (pinnedRepos.isNotEmpty()) {
                    binding.reposBar.isVisible = true
                    reposAdapter.setItems(pinnedRepos)
                } else {
                    binding.reposBar.isVisible = false
                }
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