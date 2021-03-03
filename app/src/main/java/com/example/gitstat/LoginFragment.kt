package com.example.gitstat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.gitstat.databinding.FragmentLoginBinding

class LoginFragment : Fragment(), View.OnClickListener {

    private val LOG_TAG = "DEBUG_TAG"
    private lateinit var navController: NavController

    //private lateinit var binding: LoginFragmentBinding
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        // NavController
        val hf: NavHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = hf.navController

        // Views
        binding.signInBtn.setOnClickListener(this)

        return view
    }



    // Common click listener for all buttons
    override fun onClick(v: View) {
        when (v.id) {
            R.id.signInBtn -> {
                Log.d(LOG_TAG, "login btn clicked")

                navController.navigate(R.id.actionLoginToMain)
            }
        }
    }


}