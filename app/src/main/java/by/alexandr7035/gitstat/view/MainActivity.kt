package by.alexandr7035.gitstat.view

import android.content.Intent
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
import by.alexandr7035.gitstat.BuildConfig
import by.alexandr7035.gitstat.NavGraphDirections
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.data.SyncForegroundService
import by.alexandr7035.gitstat.databinding.ActivityMainBinding
import by.alexandr7035.gitstat.core.extensions.navigateSafe
import by.alexandr7035.gitstat.core.extensions.observeNullSafe
import by.alexandr7035.gitstat.view.datasync.SyncHostFragmentDirections
import by.alexandr7035.gitstat.view.login.LoginFragmentDirections
import by.alexandr7035.gitstat.view.profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val navController: NavController by lazy(LazyThreadSafetyMode.NONE) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    private val binding: ActivityMainBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<MainViewModel>()
    private val profileViewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
            if (bottomNavVisiblePrimaryDestinations.contains(destination.id) || bottomNavVisibleDialogsDestinations.contains(destination.id)) {
                binding.bottomNavigationView.visibility = View.VISIBLE
            } else {
                binding.bottomNavigationView.visibility = View.GONE
            }

            // Allow opening drawer only on primary fragments
            if (bottomNavVisiblePrimaryDestinations.contains(destination.id)) {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }


        if (viewModel.checkIfTokenSaved()) {
            if (viewModel.checkIfCacheExists()) {
                navController.navigateSafe(LoginFragmentDirections.actionLoginFragmentToProfileFragment())
            } else {
                startSyncData()
            }
        }

        // Drawer settings
        val drawerPictureView = binding.drawerNavigationView.getHeaderView(0).findViewById<CircleImageView>(R.id.drawerProfileImage)
        val drawerLoginView = binding.drawerNavigationView.getHeaderView(0).findViewById<TextView>(R.id.drawerLoginView)
        val drawerNameView = binding.drawerNavigationView.getHeaderView(0).findViewById<TextView>(R.id.drawerNameView)
        val syncDateView = binding.drawerNavigationView.getHeaderView(0).findViewById<TextView>(R.id.syncDate)
        val resyncBtn = binding.drawerNavigationView.getHeaderView(0).findViewById<ImageView>(R.id.resyncBtn)

        profileViewModel.getUserLiveData().observeNullSafe(this, {

            Picasso.get().load(it.avatar_url).into(drawerPictureView)

            // This field can be empty
            if (it.name.isEmpty()) {
                drawerNameView.visibility = View.GONE
            } else {
                drawerNameView.visibility = View.VISIBLE
                drawerNameView.text = it.name
            }

            drawerLoginView.text = getString(R.string.login_string, it.login)

            // We can also update sync date in drawer
            // As livedata triggering means cache may have been updated
            syncDateView.text = viewModel.getCacheSyncDate().replace(" ", "\n")
        })

        binding.drawerNavigationView.setNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {

                R.id.item_logout -> {
                    navController.navigateSafe(NavGraphDirections.actionGlobalLogoutConfirmationDialog())
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }

                R.id.item_privacy_policy -> navController.navigateSafe(
                    NavGraphDirections.actionGlobalWebViewFragment(
                        getString(R.string.privacy_policy), getString(R.string.privacy_policy_url)
                    )
                )

                R.id.item_about_app -> navController.navigateSafe(
                    NavGraphDirections.actionGlobalInfoFragment(
                        getString(R.string.about_app),
                        getString(R.string.app_name_with_version, BuildConfig.VERSION_NAME),
                        getString(R.string.app_description)
                    )
                )
            }

            true
        }

        syncDateView.text = viewModel.getCacheSyncDate().replace(" ", "\n")

        resyncBtn.setOnClickListener {
            // Sync data in foreground service
            val intent = Intent(this, SyncForegroundService::class.java)
            startService(intent)

            closeDrawerMenu()
        }
    }


    // FIXME find better solution
    // than public method accessible from fragments
    fun startSyncData() {
        navController.navigateSafe(NavGraphDirections.actionGlobalSyncHostFragment())
    }

    // FIXME find better solution
    // than public method accessible from fragments
    fun startLogOut() {
        viewModel.logOut()
        FirebaseAuth.getInstance().signOut()
        navController.navigateSafe(
            NavGraphDirections
                .actionGlobalLoginFragment()
        )
    }

    // FIXME
    fun syncFinishedCallback() {
        navController.navigateSafe(SyncHostFragmentDirections.actionSyncHostFragmentToProfileFragment())
    }

    fun openDrawerMenu() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    fun closeDrawerMenu() {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

}