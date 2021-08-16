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
import androidx.navigation.fragment.NavHostFragment
import com.example.gitstat.R
import com.example.gitstat.common.SyncStatus
import com.example.gitstat.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {

    private val LOG_TAG = "DEBUG_TAG"

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentProfileBinding

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

        // Shared pref
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val user = sharedPreferences.getString(getString(R.string.shared_pref_login), "NONE")
        val token = sharedPreferences.getString(getString(R.string.shared_pref_token), "NONE")
        Log.d(LOG_TAG, "Auth '$user' with token '$token'")

        // Nav controller
        // NavController
        val hf: NavHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = hf.navController

        val viewModel = MainViewModel(requireActivity().application, "$user", "$token")


        // Update profile data
        viewModel.userLiveData.observe(viewLifecycleOwner, {

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

        })


        // Update synchronization status view
        viewModel.syncStatusLiveData.observe(viewLifecycleOwner, {
            Log.d(LOG_TAG, it)

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
                binding.syncStatusBtn.isClickable = false
                binding.syncStatusBtn.text = getString(R.string.failed)
                binding.syncStatusBtn.setBackgroundResource(R.drawable.background_sync_button_synced)
            }

        })


        binding.reposStatDetailedBtn.setOnClickListener {
            Log.d(LOG_TAG, "clicked SHOW MORE")
            navController.navigate(R.id.reposFragment)
        }

    }

}