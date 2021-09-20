package com.alexandr7035.gitstat.view.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.core.AppPreferences
import com.alexandr7035.gitstat.databinding.FragmentLoginBinding
import com.alexandr7035.gitstat.view.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment: Fragment() {

    private val LOG_TAG = "DEBUG_TAG"
    private lateinit var navController: NavController
    private var binding: FragmentLoginBinding? = null

    private val viewModel by viewModels<LoginViewModel>()

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
                ////Log.d(LOG_TAG, "Login btn pressed")

                if (! checkLoginFormIfValid()) {
                    ////Log.d(LOG_TAG, "form no valid")
                    return
                }
                else {
                    ////Log.d(LOG_TAG, "do login request")
                    token = binding!!.tokenEditText.text.toString()
                    viewModel.doLoginRequest(token)
                }

            }
        })


        // Login results handling
        viewModel.getLoginResponseCodeLiveData().observe(viewLifecycleOwner, {
            ////Log.d(LOG_TAG, "LOGIN RESULTS CODE: $it")

            when (it) {
                200 -> {
                    viewModel.saveToken(token)
                    navController.navigate(R.id.actionLoginToMain)
                }

                // FIXME
                // 404 may also be caused by wrong login data
                // when token is correct but provided user name doesn't exist on github
                401, 404 -> {
                    binding!!.tokenField.error = getString(R.string.error_wrong_data_field)
                }

                else -> {
                    Toast.makeText(requireActivity(), getString(R.string.error_cant_get_data_remote), Toast.LENGTH_LONG).show()
                }
            }

        })


    }


    private fun checkLoginFormIfValid(): Boolean {

        var isValid = true

        if (binding!!.tokenEditText.text.isNullOrBlank()) {
            binding!!.tokenField.error = getString(R.string.error_empty_field)
            isValid = false
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

}