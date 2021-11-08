package by.alexandr7035.gitstat.view.datasync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.FragmentSyncFailedNetworkBinding

class SyncFailedNetworkFragment : Fragment() {

    private var binding: FragmentSyncFailedNetworkBinding? = null
    private val viewModel by navGraphViewModels<SyncViewModel>(R.id.syncGraph) { defaultViewModelProviderFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentSyncFailedNetworkBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.retryBtn?.setOnClickListener {
            viewModel.syncData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}