package by.alexandr7035.gitstat.view.repositories.repo_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.RepositoriesListGraphDirections
import by.alexandr7035.gitstat.databinding.FragmentRepositoriesRecyclerBinding
import by.alexandr7035.gitstat.core.extensions.navigateSafe
import by.alexandr7035.gitstat.core.extensions.observeNullSafe
import by.alexandr7035.gitstat.view.repositories.RepoClickListener
import by.alexandr7035.gitstat.view.repositories.RepositoriesViewModel
import by.alexandr7035.gitstat.view.repositories.filters.RepositoriesListFiltersHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActiveRepositoriesFragment : Fragment(), RepoClickListener {

    private val viewModel by navGraphViewModels<RepositoriesViewModel>(R.id.repositoriesListGraph) { defaultViewModelProviderFactory }
    private var binding: FragmentRepositoriesRecyclerBinding? = null
    private var adapter: RepositoriesListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentRepositoriesRecyclerBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup adapter
        adapter = RepositoriesListAdapter(this)
        binding?.recycler?.adapter = adapter
        val layoutManager = LinearLayoutManager(context)

        val decoration = DividerItemDecoration(
            binding?.recycler?.context,
            layoutManager.orientation
        )

        ContextCompat.getDrawable(requireContext(), R.drawable.decoration_repository_item)?.let {
            decoration.setDrawable(it)
        }

        binding?.recycler?.addItemDecoration(decoration)
        binding?.recycler?.layoutManager = layoutManager


        viewModel.getTabRefreshedLiveData().observeNullSafe(viewLifecycleOwner, {
            // If current fragment
            // FIXME find better solution
            if (it == 0) {
                //binding!!.root.smoothScrollToPosition(0)
                binding?.recycler?.layoutManager?.scrollToPosition(0)
            }
        })
    }

    override fun onResume() {
        super.onResume()

        viewModel.getActiveRepositoriesLiveData().observeNullSafe(viewLifecycleOwner, { repos ->
            val filteredList = RepositoriesListFiltersHelper.getFilteredRepositoriesList(
                repos,
                viewModel.getRepositoriesFilters()
            )

            // When there are no repos at all
            if (repos.isEmpty()) {
                binding?.recycler?.visibility = View.GONE
                binding?.noReposStub?.visibility = View.VISIBLE
                binding?.noReposStubText?.text = getString(R.string.no_active_repos_list)
            }
            // When there are repos but list is empty because of filters
            else if (repos.isNotEmpty() && filteredList.isEmpty()) {
                binding?.recycler?.visibility = View.GONE
                binding?.noReposStub?.visibility = View.VISIBLE
                binding?.noReposStubText?.text = getString(R.string.no_active_repos_list_check_filters)
            }
            else {
                binding?.recycler?.visibility = View.VISIBLE
                binding?.noReposStub?.visibility = View.GONE
                adapter!!.setItems(filteredList)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onRepoClicked(repoId: Int) {
        findNavController().navigateSafe(RepositoriesListGraphDirections.actionGlobalRepositoryPageFragment(repoId))
    }
}