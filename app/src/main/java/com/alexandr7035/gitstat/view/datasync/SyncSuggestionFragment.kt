package com.alexandr7035.gitstat.view.datasync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.databinding.FragmentSyncSuggestionBinding
import com.alexandr7035.gitstat.view.MainActivity
import android.view.View as View1


class SyncSuggestionFragment : Fragment() {

    private var binding: FragmentSyncSuggestionBinding? = null
    private val viewModel by navGraphViewModels<SyncViewModel>(R.id.syncGraph) { defaultViewModelProviderFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View1? {
        // Inflate the layout for this fragment
        binding = FragmentSyncSuggestionBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.cacheSyncDateView?.text = getString(R.string.last_cache, viewModel.getLastCacheSyncDate())

        binding?.syncBtn?.setOnClickListener {
            viewModel.syncData()
        }

        binding?.loadCacheBtn?.setOnClickListener {
            (requireActivity() as MainActivity).syncFinishedCallback()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}