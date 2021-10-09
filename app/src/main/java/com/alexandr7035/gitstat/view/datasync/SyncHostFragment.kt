package com.alexandr7035.gitstat.view.datasync

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navGraphViewModels
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.core.DataSyncStatus
import com.alexandr7035.gitstat.databinding.FragmentSyncHostBinding
import com.alexandr7035.gitstat.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SyncHostFragment : Fragment() {

    private var binding: FragmentSyncHostBinding? = null
    private val viewModel by navGraphViewModels<SyncViewModel>(R.id.syncGraph) { defaultViewModelProviderFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSyncHostBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DEBUG_TAG", "show sync stub")

        viewModel.syncData()

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.containerView, SyncPendingFragment())
            .commit()

        viewModel.getSyncStatusLiveData().observe(viewLifecycleOwner, { status ->
            Log.d("DEBUG_TAG", "sync status updated $status")
            Toast.makeText(requireContext(), "$status", Toast.LENGTH_SHORT).show()


            when (status) {
                DataSyncStatus.SUCCESS -> {
                    (requireActivity() as MainActivity).syncFinishedCallback()
                }

                DataSyncStatus.PENDING_PROFILE -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.containerView, SyncPendingFragment.newInstance(getString(R.string.stage_profile)))
                        .commit()
                }

                DataSyncStatus.PENDING_REPOSITORIES -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.containerView, SyncPendingFragment.newInstance(getString(R.string.stage_repositories)))
                        .commit()
                }

                DataSyncStatus.PENDING_CONTRIBUTIONS -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.containerView, SyncPendingFragment.newInstance(getString(R.string.stage_contributions)))
                        .commit()
                }

                DataSyncStatus.FAILED_NETWORK_WITH_NO_CACHE -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.containerView, SyncFailedNoCacheFragment())
                        .commit()
                }

                DataSyncStatus.FAILED_NETWORK_WITH_CACHE -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.containerView, SyncFailedWithCacheFragment())
                        .commit()
                }
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}