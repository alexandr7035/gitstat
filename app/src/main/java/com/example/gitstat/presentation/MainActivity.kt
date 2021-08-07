package com.example.gitstat.presentation

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.gitstat.data.GitHubApi
import com.example.gitstat.data.remote.UserModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.gitstat.R

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val LOG_TAG = "DEBUG_TAG"

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

    }

}