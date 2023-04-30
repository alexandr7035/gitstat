package by.alexandr7035.gitstat.view.core

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.FragmentWebViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding


class WebViewFragment : Fragment(R.layout.fragment_web_view) {

    private val binding by viewBinding(FragmentWebViewBinding::bind)
    private val safeArgs by navArgs<WebViewFragmentArgs>()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = safeArgs.toolbarTitle
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.webView.settings.javaScriptEnabled = true
        loadPage()

        binding.webView.webViewClient = object : WebViewClient() {

            // Needed because onPageCommitVisible may be called after onReceivedError
            private var haveErrors: Boolean = false

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

                haveErrors = false

                binding.webView.visibility = View.GONE
                binding.progressView.visibility = View.VISIBLE
                binding.errorCard.visibility = View.GONE
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                binding.progressView.isVisible = false

                binding.webView.isVisible = !haveErrors
                binding.errorCard.isVisible = haveErrors
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                haveErrors = true

                binding.progressView.isVisible = false
                binding.webView.isVisible = false
                binding.errorCard.isVisible = true
            }
        }


        binding.reloadButton.setOnClickListener {
            loadPage()
        }

    }

    private fun loadPage() {
        binding.webView?.loadUrl(safeArgs.url)
    }

}