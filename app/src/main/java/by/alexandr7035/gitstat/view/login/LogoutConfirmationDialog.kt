package by.alexandr7035.gitstat.view.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.FragmentLogoutConfirmationDialogBinding
import by.alexandr7035.gitstat.view.MainActivity


class LogoutConfirmationDialog : DialogFragment() {

    private var binding: FragmentLogoutConfirmationDialogBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentLogoutConfirmationDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(),
        R.drawable.background_card_sync))

        binding?.cancelBtn?.setOnClickListener {
            dialog?.dismiss()
        }

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