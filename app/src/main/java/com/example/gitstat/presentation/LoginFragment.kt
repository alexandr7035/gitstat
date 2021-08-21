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
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.gitstat.data.AuthState
import com.example.gitstat.R
import com.example.gitstat.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private val LOG_TAG = "DEBUG_TAG"
    private lateinit var navController: NavController

    private lateinit var binding: FragmentLoginBinding

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Shared pref
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)

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

                if (checkLoginFormIfValid()) {
                    return
                }
                else {
                    // FIXME - DO LOGIN REQUEST
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