package by.alexandr7035.gitstat.view.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.alexandr7035.gitstat.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {

    private var binding: FragmentInfoBinding? = null
    private val safeArgs by navArgs<InfoFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding?.toolbar?.title = safeArgs.toolbarTitle
        if (safeArgs.infoTitle != null) {
            binding?.infoTitle?.text = safeArgs.infoTitle
        }
        else {
            binding?.infoTitle?.visibility = View.GONE
        }
        binding?.infoText?.text = HtmlCompat.fromHtml(safeArgs.infoText, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}