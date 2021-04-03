package com.example.gitstat

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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


        val viewModel = MainViewModel(requireActivity().application, "$user", "$token")

        viewModel.userLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.nameView.text = it.name
                binding.userIdView.text = it.login

                Picasso.get().load(it.avatar_url).into(binding.profileImageView)

                val reposCount = it.public_repos + it.total_private_repos
                binding.reposCountView.text = reposCount.toString()

                binding.followersCount.text = it.followers.toString()

            }
        })


        viewModel.emailLiveData.observe(viewLifecycleOwner, {
            binding.emailView.text = it
        })

        viewModel.updateUserData()
        viewModel.updateEmailData()

    }

}