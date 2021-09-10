package com.alexandr7035.gitstat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexandr7035.gitstat.databinding.FiltersDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RepositoriesFiltersDialog(): BottomSheetDialogFragment() {

    private var binding: FiltersDialogBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FiltersDialogBinding.inflate(inflater, container, false)
        return binding!!.root
    }

}