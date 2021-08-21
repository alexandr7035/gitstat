package com.example.gitstat.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.gitstat.R
import com.example.gitstat.common.SyncStatus
import com.example.gitstat.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {

    private val LOG_TAG = "DEBUG_TAG"

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(LOG_TAG, "profile fragment onviewcreated")

        // Shared pref
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val user = sharedPreferences.getString(getString(R.string.shared_pref_login), "NONE")
        val token = sharedPreferences.getString(getString(R.string.shared_pref_token), "NONE")
        Log.d(LOG_TAG, "Auth '$user' with token '$token'")

        // Nav controller
        // NavController
        val hf: NavHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = hf.navController

        viewModel = MainViewModel(requireActivity().application, "$user", "$token")

    }

    override fun onResume() {
        super.onResume()

        // Update profile data
        viewModel.getUserLData().observe(viewLifecycleOwner, {

            if (it != null) {

                // Show if were hidden previously
                showProfileViews()

                Picasso.get().load(it.avatar_url).into(binding.profileImageView)

                binding.nameView.text = it.name
                binding.loginView.text = it.login

                binding.idView.text = it.id.toString()

                binding.createdView.text = DateFormat.format("dd.MM.yyyy HH:mm", it.created_at)
                binding.updatedView.text = DateFormat.format("dd.MM.yyyy HH:mm", it.updated_at)

                binding.followersView.text = it.followers.toString()
                binding.locationView.text = it.location

                binding.totalReposView.text = (it.total_private_repos + it.public_repos).toString()
                binding.privateReposViev.text = it.total_private_repos.toString()
                binding.publicReposView.text = it.public_repos.toString()
            }

            // Cache db is empty
            else {
                hideProfileViews()
            }

        })


        // Update synchronization status view
        viewModel.getSyncStatusLData().observe(viewLifecycleOwner, {

            if (it == SyncStatus.PENDING) {
                binding.syncStatusBtn.isClickable = false
                binding.syncStatusBtn.text = getString(R.string.loading)
                binding.syncStatusBtn.setBackgroundResource(R.drawable.background_sync_button_pending)
            }

            else if (it == SyncStatus.SUCCESS) {
                binding.syncStatusBtn.isClickable = true
                binding.syncStatusBtn.text = getString(R.string.synced)
                binding.syncStatusBtn.setBackgroundResource(R.drawable.background_sync_button_synced)
            }

            else if (it == SyncStatus.FAILED) {
                // FIXME currently disabled. Fix when implement refresh
                binding.syncStatusBtn.isClickable = true
                binding.syncStatusBtn.text = getString(R.string.failed)
                binding.syncStatusBtn.setBackgroundResource(R.drawable.background_sync_button_failed)
            }

        })


        binding.reposStatDetailedBtn.setOnClickListener {
            navController.navigate(R.id.reposFragment)
        }

        binding.logOutBtn.setOnClickListener {

            Log.d(LOG_TAG, "log out")

            // Reset shared prefs
            val editor = sharedPreferences.edit()
            editor.putString(
                getString(R.string.shared_pref_login),
                getString(R.string.shared_pref_default_string_value)
            )

            editor.putString(
                getString(R.string.shared_pref_token),
                getString(R.string.shared_pref_default_string_value)
            )

            editor.commit()

            // Clear room cache
            viewModel.clearCache()

            navController.navigate(R.id.loginFragment)
        }
    }


    private fun hideProfileViews() {
        binding.headerCard.visibility = View.GONE
        binding.profileSummaryCard.visibility = View.GONE
        binding.reposSummaryCard.visibility = View.GONE
    }

    private fun showProfileViews() {
        binding.headerCard.visibility = View.VISIBLE
        binding.profileSummaryCard.visibility = View.VISIBLE
        binding.reposSummaryCard.visibility = View.VISIBLE
    }

}