package com.alexandr7035.gitstat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexandr7035.gitstat.databinding.FiltersDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RepositoriesFiltersDialog(private val currentFilters: ReposFilters, private val filtersUpdateObserver: FiltersUpdateObserver): BottomSheetDialogFragment() {

    private var binding: FiltersDialogBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FiltersDialogBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load current filters settings
        setupFiltersViews()

        binding!!.applyButton.setOnClickListener {

            val newFilters = ReposFilters()

            when (binding!!.includeRepositoriesRadioGroup.checkedRadioButtonId) {
                binding!!.includeAllBtn.id -> {
                    newFilters.filterPrivacy = ReposFilters.FilterPrivacy.ALL_REPOS
                }

                binding!!.includePrivateOnlyBtn.id -> {
                    newFilters.filterPrivacy = ReposFilters.FilterPrivacy.PRIVATE_REPOS_ONLY
                }

                binding!!.includePublicOnlyBtn.id -> {
                    newFilters.filterPrivacy = ReposFilters.FilterPrivacy.PUBLIC_REPOS_ONLY
                }
            }

            filtersUpdateObserver.onFiltersUpdated(newFilters)
            dismiss()
        }
    }


    private fun setupFiltersViews() {
        when (currentFilters.filterPrivacy) {
            ReposFilters.FilterPrivacy.PRIVATE_REPOS_ONLY -> binding!!.includePrivateOnlyBtn.isChecked = true
            ReposFilters.FilterPrivacy.PUBLIC_REPOS_ONLY -> binding!!.includePublicOnlyBtn.isChecked = true
            ReposFilters.FilterPrivacy.ALL_REPOS -> binding!!.includeAllBtn.isChecked = true
        }

        when (currentFilters.sortingType) {
            ReposFilters.SortingType.BY_REPO_NAME -> binding!!.sortByRepositoryNameBtn.isChecked = true
            ReposFilters.SortingType.BY_REPO_CREATION_DATE -> binding!!.sortByCreationDateBtn.isChecked = true
        }

        when (currentFilters.sortingOrder) {
            ReposFilters.SortingOrder.ASCENDING_MODE -> binding!!.sortAscendingBtn.isChecked = true
            ReposFilters.SortingOrder.DESCENDING_MODE-> binding!!.sortDescendingBtn.isChecked = true
        }
    }


    interface FiltersUpdateObserver {
        fun onFiltersUpdated(filters: ReposFilters)
    }

}