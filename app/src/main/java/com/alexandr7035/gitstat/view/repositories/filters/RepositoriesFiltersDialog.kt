package com.alexandr7035.gitstat.view.repositories.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.databinding.FiltersDialogBinding
import com.alexandr7035.gitstat.view.repositories.RepositoriesViewModel
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoriesFiltersDialog : BottomSheetDialogFragment() {
    private var binding: FiltersDialogBinding? = null
    private var checkedLanguages = HashSet<String>()

    private val viewModel by navGraphViewModels<RepositoriesViewModel>(R.id.repositoriesListGraph) { defaultViewModelProviderFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // Allow to fill full screen
        // Also disable dragging (for recyclerview correct work)
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            isDraggable = false
        }

        binding = FiltersDialogBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load current filters settings
        setupFiltersViews(viewModel.getRepositoriesFilters())

        val adapter = LanguagesAdapter(checkedLanguages)
        binding!!.languagesFilterRecyclerView.adapter = adapter
        binding!!.languagesFilterRecyclerView.layoutManager = FlexboxLayoutManager(context)

        // FIXME
        // Now build langs list depending on ALL the repos of a user
        // Create different filters for active/archived later
        viewModel.getAllRepositoriesListLiveData().observe(viewLifecycleOwner, { repos ->
            adapter.setItems(viewModel.getLanguagesForReposList(repos))
        })

        binding!!.applyButton.setOnClickListener {

            val newFilters = ReposFilters()

            when (binding!!.filterPrivacyRadioGroup.checkedRadioButtonId) {
                binding!!.filterPrivacyIncludeAllBtn.id -> {
                    newFilters.filterPrivacy = ReposFilters.FilterPrivacy.ALL_REPOS
                }

                binding!!.filterPrivacyIncludePrivateOnlyBtn.id -> {
                    newFilters.filterPrivacy = ReposFilters.FilterPrivacy.PRIVATE_REPOS_ONLY
                }

                binding!!.filterPrivacyIncludePublicOnlyBtn.id -> {
                    newFilters.filterPrivacy = ReposFilters.FilterPrivacy.PUBLIC_REPOS_ONLY
                }
            }

            when (binding!!.filterForksRadioGroup.checkedRadioButtonId) {
                binding!!.filterForksIncludeAllBtn.id -> {
                    newFilters.filterForks = ReposFilters.FilterForks.ALL_REPOS
                }

                binding!!.filterForksIncludeForksOnlyBtn.id -> {
                    newFilters.filterForks = ReposFilters.FilterForks.FORKS_ONLY
                }

                binding!!.filterForksExcludeForksBtn.id -> {
                    newFilters.filterForks = ReposFilters.FilterForks.EXCLUDE_FORKS
                }
            }

            when (binding!!.sortingTypeRadioGroup.checkedRadioButtonId) {
                binding!!.sortByRepositoryNameBtn.id -> {
                    newFilters.sortingType = ReposFilters.SortingType.BY_REPO_NAME
                }

                binding!!.sortByCreationDateBtn.id -> {
                    newFilters.sortingType = ReposFilters.SortingType.BY_REPO_CREATION_DATE
                }
            }


            when (binding!!.sortingOrderRadioGroup.checkedRadioButtonId) {
                binding!!.sortAscendingBtn.id -> {
                    newFilters.sortingOrder = ReposFilters.SortingOrder.ASCENDING_MODE
                }

                binding!!.sortDescendingBtn.id -> {
                    newFilters.sortingOrder = ReposFilters.SortingOrder.DESCENDING_MODE
                }
            }

            newFilters.filterLanguages = adapter.getCheckedLanguages()

            // Save filters to persistent storage
            viewModel.saveRepositoriesFilters(newFilters)

            // Reload the lists from cache
            // FIXME separate for tabs
            viewModel.updateAllRepositoriesLiveData()
            viewModel.updateArchivedRepositoriesLiveData()
            viewModel.updateActiveRepositoriesLiveData()

            dismiss()
        }


        // Clear buttons
        binding!!.clearForksAndPrivacyFiltersBtn.setOnClickListener {
            binding!!.filterPrivacyRadioGroup.check(R.id.filterPrivacyIncludeAllBtn)
            binding!!.filterForksRadioGroup.check(R.id.filterForksIncludeAllBtn)
        }

        binding!!.clearLanguagesBtn.setOnClickListener {
            adapter.clearCheckedLanguages()
        }
    }


    private fun setupFiltersViews(currentFilters: ReposFilters) {
        when (currentFilters.filterPrivacy) {
            ReposFilters.FilterPrivacy.PRIVATE_REPOS_ONLY -> binding!!.filterPrivacyIncludePrivateOnlyBtn.isChecked = true
            ReposFilters.FilterPrivacy.PUBLIC_REPOS_ONLY -> binding!!.filterPrivacyIncludePublicOnlyBtn.isChecked = true
            ReposFilters.FilterPrivacy.ALL_REPOS -> binding!!.filterPrivacyIncludeAllBtn.isChecked = true
        }

        when (currentFilters.filterForks) {
            ReposFilters.FilterForks.FORKS_ONLY -> binding!!.filterForksIncludeForksOnlyBtn.isChecked = true
            ReposFilters.FilterForks.EXCLUDE_FORKS -> binding!!.filterForksExcludeForksBtn.isChecked = true
            ReposFilters.FilterForks.ALL_REPOS-> binding!!.filterForksIncludeAllBtn.isChecked = true
        }

        when (currentFilters.sortingType) {
            ReposFilters.SortingType.BY_REPO_NAME -> binding!!.sortByRepositoryNameBtn.isChecked = true
            ReposFilters.SortingType.BY_REPO_CREATION_DATE -> binding!!.sortByCreationDateBtn.isChecked = true
        }

        when (currentFilters.sortingOrder) {
            ReposFilters.SortingOrder.ASCENDING_MODE -> binding!!.sortAscendingBtn.isChecked = true
            ReposFilters.SortingOrder.DESCENDING_MODE -> binding!!.sortDescendingBtn.isChecked = true
        }

        checkedLanguages = currentFilters.filterLanguages as HashSet<String>
    }

}