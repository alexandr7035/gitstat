package com.example.gitstat.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.gitstat.R
import com.example.gitstat.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private val LOG_TAG = "DEBUG_TAG"
    private lateinit var navController: NavController

    private lateinit var binding: FragmentLoginBinding

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var viewModel: MainViewModel
    private val loginResponseLiveData = MutableLiveData<Int>()

    private lateinit var token: String
    private lateinit var login: String

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Shared pref
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val user = sharedPreferences.getString(getString(R.string.shared_pref_login), "NONE")
        val token = sharedPreferences.getString(getString(R.string.shared_pref_token), "NONE")

        // ViewModel
        viewModel = MainViewModel(requireActivity().application, "$user", "$token")

        // NavController
        val hf: NavHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = hf.navController

        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.loginEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    if (!TextUtils.isEmpty(binding.loginField.error)) {
                        binding.loginField.error = null
                        binding.loginField.isErrorEnabled = false
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


        binding.tokenEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    if (!TextUtils.isEmpty(binding.tokenField.error)) {
                        binding.tokenField.error = null
                        binding.tokenField.isErrorEnabled = false
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


        binding.signInBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                Log.d(LOG_TAG, "Login btn pressed")

                if (! checkLoginFormIfValid()) {
                    Log.d(LOG_TAG, "form no valid")
                    return
                }
                else {
                    Log.d(LOG_TAG, "do login request")
                    login = binding.loginEditText.text.toString()
                    token = binding.tokenEditText.text.toString()
                    viewModel.doLoginRequest(login, token)
                }

            }
        })


        // Login results handling
        viewModel.getLoginResponseCodeLiveData().observe(viewLifecycleOwner, {
            Log.d(LOG_TAG, "LOGIN RESULTS CODE: $it")

            when (it) {
                200 -> {
                    val prefEditor = sharedPreferences.edit()
                    prefEditor.putString(
                        getString(R.string.shared_pref_token), token)

                    prefEditor.putString(
                        getString(R.string.shared_pref_login), login)

                    prefEditor.commit()

                    navController.navigate(R.id.actionLoginToMain)
                }

                401 -> {
                    binding.tokenField.error = getString(R.string.error_wrong_data_field)
                    binding.loginField.error = getString(R.string.error_wrong_data_field)
                }

                else -> {
                    Toast.makeText(requireActivity(), getString(R.string.error_cant_get_data_remote), Toast.LENGTH_LONG).show()
                }
            }

        })


    }


    private fun checkLoginFormIfValid(): Boolean {

        var isValid = true

        if (binding.loginEditText.text.isNullOrBlank()) {
            binding.loginField.error = getString(R.string.error_empty_field)
            isValid = false
        }

        if (binding.tokenEditText.text.isNullOrBlank()) {
            binding.tokenField.error = getString(R.string.error_empty_field)
            isValid = false
        }

        return isValid
    }

}