package by.alexandr7035.gitstat.view.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.alexandr7035.gitstat.databinding.FragmentWebViewBinding


class WebViewFragment : Fragment() {

    private var binding: FragmentWebViewBinding? = null

    private var toolbarTitle: String? = null
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            toolbarTitle = it.getString("toolbar_title")
            url = it.getString("url")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(toolbarTitle: String, url: String) =
            WebViewFragment().apply {
                arguments = Bundle().apply {
                    putString("toolbar_title", toolbarTitle)
                    putString("url", url)
                }
            }
    }
}