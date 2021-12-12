package by.alexandr7035.gitstat.view.datasync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.alexandr7035.gitstat.BuildConfig
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.core.DataSyncStatus
import by.alexandr7035.gitstat.core.extensions.observeNullSafe
import by.alexandr7035.gitstat.databinding.FragmentSyncHostBinding
import by.alexandr7035.gitstat.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SyncHostFragment : Fragment() {

    private var binding: FragmentSyncHostBinding? = null
    private val viewModel by viewModels<SyncViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSyncHostBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.syncData()

        viewModel.getSyncStatusLiveData().observeNullSafe(viewLifecycleOwner, { status ->
            Timber.d("sync status updated $status")

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

                DataSyncStatus.FAILED_NETWORK -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.containerView, SyncFailedNetworkFragment())
                        .commit()
                }

                DataSyncStatus.AUTHORIZATION_ERROR -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.containerView, SyncFailedAuthorizationError())
                        .commit()
                }
            }

        })

        binding?.version?.text = getString(R.string.app_name_with_version, BuildConfig.VERSION_NAME)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}