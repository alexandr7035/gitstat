package com.alexandr7035.gitstat.presentation

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.alexandr7035.gitstat.R

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val LOG_TAG = "DEBUG_TAG"

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var user: String
    private lateinit var token: String

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
            if (destination.id == R.id.loginFragment) {
                bottomNavigationView.visibility = View.GONE
            } else {
                bottomNavigationView.visibility = View.VISIBLE
            }
        }



        // Shared pref
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        user = sharedPreferences.getString(getString(R.string.shared_pref_login), "NONE")!!
        token = sharedPreferences.getString(getString(R.string.shared_pref_token), "NONE")!!

        // Dynamically change initial fragment
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        if (token != getString(R.string.shared_pref_default_string_value) && user != getString(R.string.shared_pref_default_string_value)) {
            navGraph.startDestination = R.id.profileFragment
        }
        else {
            navGraph.startDestination = R.id.loginFragment
        }

        navController.graph = navGraph

    }

}