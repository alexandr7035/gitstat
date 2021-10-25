package by.alexandr7035.gitstat.view.datasync

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.FragmentSyncFailedAuthorizationErrorBinding
import by.alexandr7035.gitstat.view.MainActivity


class SyncFailedAuthorizationError : Fragment() {

    private var binding: FragmentSyncFailedAuthorizationErrorBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentSyncFailedAuthorizationErrorBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.logOutBtn?.setOnClickListener {
            // FIXME find better solution
            (requireActivity() as MainActivity).startLogOut()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}