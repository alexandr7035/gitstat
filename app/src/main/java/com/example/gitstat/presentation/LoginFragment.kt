package com.example.gitstat.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
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

class LoginFragment : Fragment(), View.OnClickListener {

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

        if (authenticateByToken() == AuthState.AUTH_SUCCESS) {
            // Go to main screen
            navController.navigate(R.id.actionLoginToMain)
        }

        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        // Views
        binding.signInBtn.setOnClickListener(this)

        return view
    }



    // Common click listener for all buttons
    override fun onClick(v: View) {
        when (v.id) {
            R.id.signInBtn -> {
                Log.d(LOG_TAG, "login btn clicked")


                val prefEditor = sharedPreferences.edit()

                prefEditor.putString(getString(R.string.shared_pref_login), binding.loginEditText.text.toString())
                prefEditor.putString(getString(R.string.shared_pref_token), binding.tokenEditText.text.toString())
                prefEditor.apply()

                //Toast.makeText(requireActivity(), sharedPreferences.getString(getString(R.string.shared_pref_token), "NONE"), Toast.LENGTH_SHORT).show()

                // Go to main screen
                navController.navigate(R.id.actionLoginToMain)
            }
        }
    }


    // FIXME
    // This is a test variant than doesn't check token
    private fun authenticateByToken(): Int {

        val token = sharedPreferences.getString(getString(R.string.shared_pref_token), "NONE")

        if (token != "NONE") {
            Log.d(LOG_TAG, "log with token $token")
            return AuthState.AUTH_SUCCESS
        }

        return AuthState.AUTH_FAILED
    }

}