package com.alexandr7035.gitstat.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.core.SyncStatus
import com.alexandr7035.gitstat.databinding.ActivityMainBinding
import com.alexandr7035.gitstat.view.login.LoginViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // NavController
        val hf: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = hf.navController

        // Setting Navigation Controller with the BottomNavigationView
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.BottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            // FIXME find better solution
            when (destination.id) {
                R.id.profileFragment -> bottomNavigationView.visibility = View.VISIBLE
                R.id.reposOverviewFragment -> bottomNavigationView.visibility = View.VISIBLE
                R.id.contributionsFragment -> bottomNavigationView.visibility = View.VISIBLE
                else -> bottomNavigationView.visibility = View.GONE
            }
        }


        // Dynamically change initial fragment
//        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        if (viewModel.checkIfLoggedIn()) {
            // FIXME
            viewModel.syncData()
        }
        else {
            updateStartDestination(R.id.loginFragment)
//            navGraph.startDestination = R.id.loginFragment
        }

//        navController.graph = navGraph


        viewModel.getSyncStatusLiveData().observe(this, { status ->

            when (status) {
                SyncStatus.SUCCESS -> {
                    Log.d("DEBUG_TAG", "SYNC success")
                    navController.navigate(R.id.actionLoginToMain)
                    binding.progressView.visibility = View.GONE

                    updateStartDestination(R.id.profileFragment)
                }

                SyncStatus.PENDING -> {
                    binding.progressView.visibility = View.VISIBLE
                    Log.d("DEBUG_TAG", "SYNC pending")
                }

                SyncStatus.FAILED -> {
                    // FIXME
                    // 2 fail statuses
                    binding.progressView.visibility = View.VISIBLE
                    Log.d("DEBUG_TAG", "SYNC failed")
                }
            }

        })

    }

    // To disable back to login fragment
    private fun updateStartDestination(fragmentID: Int) {
        // Dynamically change initial fragment
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        navGraph.startDestination = fragmentID

        navController.graph = navGraph
    }


    fun loginCallback() {
        viewModel.syncData()
    }

}