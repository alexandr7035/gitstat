package by.alexandr7035.gitstat.view.datasync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.FragmentSyncFailedWithCacheBinding
import by.alexandr7035.gitstat.view.MainActivity

class SyncFailedWithCacheFragment : Fragment() {

    private var binding: FragmentSyncFailedWithCacheBinding? = null
    private val viewModel by navGraphViewModels<SyncViewModel>(R.id.syncGraph) { defaultViewModelProviderFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentSyncFailedWithCacheBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.cacheSyncDateView?.text = getString(R.string.last_cache, viewModel.getLastCacheSyncDate())

        binding?.retryBtn?.setOnClickListener {
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