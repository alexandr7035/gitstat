package com.example.gitstat.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.gitstat.MainViewModel
import com.example.gitstat.R
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

        viewModel.userLiveData.observe(viewLifecycleOwner, {
            if (it != null) {

                // Header
                binding.nameView.text = it.name
                binding.userIdView.text = it.login
                Picasso.get().load(it.avatar_url).into(binding.profileImageView)

                // Summary card
                binding.idView.text = it.id.toString()
                // FIXME change format
                binding.createdView.text = it.created_at
                binding.updatedView.text = it.updated_at
                binding.followersView.text = it.followers.toString()
                binding.locationView.text = it.location

            }
        })

        viewModel.reposLiveData.observe(viewLifecycleOwner, {

            if (it != null) {

                var totalReposCount = it.size
                var privateReposCount = 0
                var publicReposCount = 0

                it.forEach {
                    if (it.private){
                        privateReposCount += 1
                    }
                }

                publicReposCount = totalReposCount - privateReposCount

                binding.totalReposView.text = totalReposCount.toString()
                binding.publicReposView.text = publicReposCount.toString()
                binding.privateReposViev.text = privateReposCount.toString()

            }

        })


        viewModel.updateUserData()
        viewModel.updateRepositoriesData()


        binding.reposStatDetailedBtn.setOnClickListener {
            Log.d(LOG_TAG, "clicked SHOW MORE")
            navController.navigate(R.id.reposFragment)
        }

        // Show API errors
        viewModel.msgLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })

    }

}