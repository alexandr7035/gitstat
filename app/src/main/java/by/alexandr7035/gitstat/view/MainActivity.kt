package by.alexandr7035.gitstat.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.ActivityMainBinding
import by.alexandr7035.gitstat.view.profile.ProfileViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    private val profileViewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // NavController
        val hf: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = hf.navController

        // Setting Navigation Controller with the BottomNavigationView )
        binding.bottomNavigationView.setupWithNavController(navController)

        // Similar with drawer
        binding.drawerNavigationView.setupWithNavController(navController)

        val bottomNavVisiblePrimaryDestinations = listOf(
            R.id.profileFragment,
            R.id.reposOverviewFragment,
            R.id.contributionsFragment,
            R.id.logoutConfirmationDialog
        )

        val bottomNavVisibleDialogsDestinations = listOf(
            R.id.logoutConfirmationDialog,
            R.id.infoDialogFragment
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Show bottom nav on primary fragments and their dialogs
            if (bottomNavVisiblePrimaryDestinations.contains(destination.id) ||  bottomNavVisibleDialogsDestinations.contains(destination.id)) {
                binding.bottomNavigationView.visibility = View.VISIBLE
            }
            else {
                binding.bottomNavigationView.visibility = View.GONE
            }

            // Allow opening drawer only on primary fragments
            if (bottomNavVisiblePrimaryDestinations.contains(destination.id)) {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
            else {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }


        if (viewModel.checkIfTokenSaved()) {
            startSyncData()
        }
        else {
            // TODO
        }


        // Drawer settings
        val drawerPictureView = binding.drawerNavigationView.getHeaderView(0).findViewById<CircleImageView>(R.id.drawerProfileImage)
        val drawerLoginView = binding.drawerNavigationView.getHeaderView(0).findViewById<TextView>(R.id.drawerLoginView)
        val drawerNameView = binding.drawerNavigationView.getHeaderView(0).findViewById<TextView>(R.id.drawerNameView)
        val syncDateView = binding.drawerNavigationView.getHeaderView(0).findViewById<TextView>(R.id.syncDate)
        val resyncBtn = binding.drawerNavigationView.getHeaderView(0).findViewById<ImageView>(R.id.resyncBtn)

        profileViewModel.getUserLiveData().observe(this, {

            if (it != null) {

                Picasso.get().load(it.avatar_url).into(drawerPictureView)

                // This field can be empty
                if (it.name.isEmpty()) {
                    drawerNameView.visibility = View.GONE
                } else {
                    drawerNameView.visibility = View.VISIBLE
                    drawerNameView.text = it.name
                }

                drawerLoginView.text = "@${it.login}"
            }
        })

        binding.drawerNavigationView.setNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.item_logout ->  {
                    navController.navigate(R.id.action_global_logoutConfirmationDialog)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }

                R.id.item_privacy_policy -> navController.navigate(MainActivityDirections.actionGlobalWebViewFragment(
                    getString(R.string.privacy_policy),
                    getString(R.string.privacy_policy_url)
                ))

                R.id.item_about_app -> navController.navigate(R.id.action_global_aboutAppFragment)
            }

            true
        }

        syncDateView.text = viewModel.getCacheSyncDate().replace(" ", "\n")

        resyncBtn.setOnClickListener {
            navController.navigate(R.id.action_global_syncGraph)
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

    fun openDrawerMenu() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

}