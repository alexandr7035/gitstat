package by.alexandr7035.gitstat.view.datasync

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.alexandr7035.gitstat.BuildConfig
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.core.DataSyncStatus
import by.alexandr7035.gitstat.core.ErrorType
import by.alexandr7035.gitstat.core.extensions.observeNullSafe
import by.alexandr7035.gitstat.databinding.FragmentSyncHostBinding
import by.alexandr7035.gitstat.view.MainActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class SyncHostFragment : Fragment(R.layout.fragment_sync_host) {
    private val binding by viewBinding(FragmentSyncHostBinding::bind)
    private val viewModel by viewModels<SyncViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animation= AnimationUtils.loadAnimation(requireContext(), R.anim.anim_rotation)
        animation.interpolator = LinearInterpolator()
        animation.repeatCount = Animation.INFINITE
//        animation.duration = 1400

        binding.stageView.loaderPic.startAnimation(animation)

        binding.stageView.root.isVisible = true
        binding.errorView.root.isVisible = false

        viewModel.syncData()

        viewModel.getSyncStatusLiveData().observeNullSafe(viewLifecycleOwner) { status ->
            binding.stageView.root.isVisible = true
            binding.errorView.root.isVisible = false

            Timber.d("sync status updated $status")
            when (status) {
                is DataSyncStatus.PendingProfile -> {
                    binding.stageView.syncStageDesc.text = getString(R.string.stage_profile)
                }

                is DataSyncStatus.PendingContributions -> {
                    binding.stageView.syncStageDesc.text = getString(R.string.stage_contributions)
                }

                is DataSyncStatus.PendingRepos -> {
                    binding.stageView.syncStageDesc.text = getString(R.string.stage_repositories)
                }

                is DataSyncStatus.Failure -> {
                    binding.stageView.root.isVisible = false
                    binding.errorView.root.isVisible = true

                    when (status.error) {
                        ErrorType.FAILED_CONNECTION -> {
                            binding.errorView.errorExplanation.text = getString(R.string.error_cant_get_data_remote)
                            binding.errorView.errorActionButton.text = getString(R.string.try_again)
                            binding.errorView.errorActionButton.setOnClickListener {
                                // Try sync again
                                (requireActivity() as MainActivity).startSyncData()
                            }
                        }

                        else -> {
                            binding.errorView.errorExplanation.text = getString(R.string.unknown_error_try_login)
                            binding.errorView.errorActionButton.text = getString(R.string.got_it)

                            binding.errorView.errorActionButton.setOnClickListener {
                                (requireActivity() as MainActivity).startLogOut()
                            }
                        }
                    }
                }

                is DataSyncStatus.Success -> {
                    // TODO fix conditional navigation
                    (requireActivity() as MainActivity).syncFinishedCallback()
                }
            }

        }

        binding.version.text = getString(R.string.app_name_with_version, BuildConfig.VERSION_NAME)
    }
}