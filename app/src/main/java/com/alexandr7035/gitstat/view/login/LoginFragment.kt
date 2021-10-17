package com.alexandr7035.gitstat.view.login

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.alexandr7035.gitstat.BuildConfig
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.core.AuthStatus
import com.alexandr7035.gitstat.databinding.FragmentLoginBinding
import com.alexandr7035.gitstat.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment: Fragment() {

    private lateinit var navController: NavController
    private var binding: FragmentLoginBinding? = null

    private val viewModel by viewModels<AuthViewModel>()

    private lateinit var token: String

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // NavController
        val hf: NavHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = hf.navController

        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.tokenEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    if (!TextUtils.isEmpty(binding!!.tokenField.error)) {
                        binding!!.tokenField.error = null
                        binding!!.tokenField.isErrorEnabled = false
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


        binding!!.signInBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                if (! checkLoginFormIfValid()) {
                    Timber.d("form is not valid")
                    return
                }

                else {
                    token = binding!!.tokenEditText.text.toString()

                    // Show progress stub and do login
                    binding?.loginProgressView?.visibility = View.VISIBLE

                    viewModel.saveToken(token)
                    viewModel.authorize()
                }

            }
        })


        viewModel.getAuthResultLiveData().observe(viewLifecycleOwner, { status ->

            // Hide progress
            binding?.loginProgressView?.visibility = View.GONE

            when (status) {
                AuthStatus.SUCCESS -> {
                    // FIXME Not good. Find better solution
                    (requireActivity() as MainActivity).startSyncData()
                }

                AuthStatus.FAILED_NETWORK -> {
                    viewModel.clearToken()
                    Toast.makeText(requireActivity(), getString(R.string.error_cant_get_data_remote), Toast.LENGTH_LONG).show()
                }

                AuthStatus.FAILED_CREDENTIALS -> {
                    viewModel.clearToken()
                    binding!!.tokenField.error = getString(R.string.error_wrong_token_field)
                }

                AuthStatus.UNKNOWN_ERROR -> {
                    viewModel.clearToken()
                    Toast.makeText(requireActivity(), getString(R.string.error_unknown_auth), Toast.LENGTH_LONG).show()
                }
            }

        })


        val obtainTokenFullText = getString(R.string.obtain_token_full_text)
        val obtainTokenLinkText = getString(R.string.obtain_token_clickable)
        val obtainTokenSpannable = SpannableString(obtainTokenFullText)

        val obtainTokenClickable = object : ClickableSpan() {
            override fun onClick(widget: View) {
                showTokenInstructions()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.color = ContextCompat.getColor(requireContext(), R.color.gray_500)
            }
        }

        obtainTokenSpannable.setSpan(
            obtainTokenClickable,
            obtainTokenFullText.indexOf(obtainTokenLinkText),
            obtainTokenFullText.indexOf(obtainTokenLinkText) + obtainTokenLinkText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        obtainTokenSpannable.setSpan(
            StyleSpan(Typeface.BOLD),
            obtainTokenFullText.indexOf(obtainTokenLinkText),
            obtainTokenFullText.indexOf(obtainTokenLinkText) + obtainTokenLinkText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding?.obtainTokenView?.apply {
            text = obtainTokenSpannable
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }


        binding?.version?.text = getString(R.string.app_name_with_version, BuildConfig.VERSION_NAME)
    }


    private fun checkLoginFormIfValid(): Boolean {

        var isValid = true

        if (binding!!.tokenEditText.text.isNullOrBlank()) {
            binding!!.tokenField.error = getString(R.string.error_empty_field)
            isValid = false
        }

        return isValid
    }

    private fun showTokenInstructions() {
        navController.navigate(R.id.action_loginFragment_to_webViewFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

}