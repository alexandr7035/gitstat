package by.alexandr7035.gitstat.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

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
                R.id.logoutConfirmationDialog -> bottomNavigationView.visibility = View.VISIBLE
                else -> bottomNavigationView.visibility = View.GONE
            }
        }


        if (viewModel.checkIfTokenSaved()) {
            startSyncData()
        }
        else {
            // TODO
        }
    }


    // FIXME find better solution
    // than public method accessible from fragments
    fun startSyncData() {
        navController.navigate(R.id.action_loginFragment_to_syncGraph)
    }

    // FIXME find better solution
    // than public method accessible from fragments
    fun startLogOut() {
        viewModel.clearCache()
        viewModel.clearToken()
        navController.navigate(R.id.action_logoutConfirmationDialog_to_loginFragment)
    }

    fun syncFinishedCallback() {
        navController.navigate(R.id.action_global_profileFragment)
    }

}