package com.alexandr7035.gitstat.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.view.login.LoginViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                else -> bottomNavigationView.visibility = View.GONE
            }
        }


        // Dynamically change initial fragment
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        if (viewModel.checkIfLoggedIn()) {
            navGraph.startDestination = R.id.profileFragment
        }
        else {
            navGraph.startDestination = R.id.loginFragment
        }

        navController.graph = navGraph

    }

}