package com.alexandr7035.gitstat.view.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.core.SyncStatus
import com.alexandr7035.gitstat.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var binding: FragmentProfileBinding? = null
    private lateinit var navController: NavController

    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigation controller
        val hf: NavHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = hf.navController

    }

    override fun onResume() {
        super.onResume()

        // Update profile data
        viewModel.getUserLiveData().observe(viewLifecycleOwner, {

            if (it != null) {

                // Show if were hidden previously
                showProfileViews()

                Picasso.get().load(it.avatar_url).into(binding!!.profileImageView)

                binding!!.nameView.text = it.name
                binding!!.loginView.text = it.login

                binding!!.idView.text = it.id.toString()

                binding!!.createdView.text = DateFormat.format("dd.MM.yyyy HH:mm", it.created_at)
                binding!!.updatedView.text = DateFormat.format("dd.MM.yyyy HH:mm", it.updated_at)

                binding!!.followersView.text = it.followers.toString()
                binding!!.locationView.text = it.location

                binding!!.totalReposView.text = (it.total_private_repos + it.public_repos).toString()
                binding!!.privateReposView.text = it.total_private_repos.toString()
                binding!!.publicReposView.text = it.public_repos.toString()
            }

            // Cache db is empty
            else {
                hideProfileViews()
            }

        })


        // Update synchronization status view
        viewModel.getSyncStatusLiveData().observe(viewLifecycleOwner, {
            when (it!!) {
                SyncStatus.PENDING -> {
                    binding!!.syncStatusView.setBackgroundResource(R.drawable.background_sync_button_pending)
                }
                SyncStatus.SUCCESS -> {
//                    binding.swipeRefreshLayout.isRefreshing = false
                    binding!!.syncStatusView.setBackgroundResource(R.drawable.background_sync_button_synced)
                }
                SyncStatus.FAILED -> {
//                    binding.swipeRefreshLayout.isRefreshing = false
                    binding!!.syncStatusView.setBackgroundResource(R.drawable.background_sync_button_failed)
                }
            }
        })

        binding!!.reposStatDetailedBtn.setOnClickListener {
            navController.navigate(R.id.reposFragment)
        }

        binding!!.logOutBtn.setOnClickListener {
            //viewModel.doLogOut()
            navController.navigate(R.id.loginFragment)
        }

        viewModel.syncUserDta()
    }


    private fun hideProfileViews() {
        binding!!.headerCard.visibility = View.GONE
        binding!!.profileSummaryCard.visibility = View.GONE
        binding!!.reposSummaryCard.visibility = View.GONE
    }

    private fun showProfileViews() {
        binding!!.headerCard.visibility = View.VISIBLE
        binding!!.profileSummaryCard.visibility = View.VISIBLE
        binding!!.reposSummaryCard.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

}