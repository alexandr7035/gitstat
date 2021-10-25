package by.alexandr7035.gitstat.view.datasync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.alexandr7035.gitstat.databinding.FragmentSyncPendingBinding

class SyncPendingFragment : Fragment() {

    private val SYNC_STAGE = "SYNC_STAGE"

    private var binding: FragmentSyncPendingBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSyncPendingBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.syncStageView?.text = arguments?.getString(SYNC_STAGE)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    companion object {
        @JvmStatic
        fun newInstance(syncStage: String) =
            SyncPendingFragment().apply {
                arguments = Bundle().apply {
                    putString(SYNC_STAGE, syncStage)
                }
            }
    }

}