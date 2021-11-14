package by.alexandr7035.gitstat.view.datasync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.alexandr7035.gitstat.databinding.FragmentSyncFailedNetworkBinding
import by.alexandr7035.gitstat.view.MainActivity

class SyncFailedNetworkFragment : Fragment() {

    private var binding: FragmentSyncFailedNetworkBinding? = null

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
            (requireActivity() as MainActivity).startSyncData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}