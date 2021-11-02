package by.alexandr7035.gitstat.view.core.info_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.FragmentInfoDialogBinding

class InfoDialogFragment : DialogFragment() {

    private var binding: FragmentInfoDialogBinding? = null
    private val safeArgs by navArgs<InfoDialogFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentInfoDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(requireContext(),
            R.drawable.background_card_sync))

        binding?.dismissButton?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.dialogTitle?.text = safeArgs.dialogTitle
        binding?.mainText?.text = safeArgs.dialogText
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}