package com.example.gitstat

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gitstat.databinding.FragmentReposBinding
import com.example.gitstat.model.RepositoryModel
import com.example.gitstat.model.UserModel


class ReposFragment : Fragment() {

    private val LOG_TAG = "DEBUG_TAG"
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var reposList: ArrayList<RepositoryModel>
    private lateinit var userModel: UserModel

    private lateinit var binding: FragmentReposBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentReposBinding.inflate(inflater, container, false)

        return binding.root
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

                val reposCount = it.public_repos + it.total_private_repos

                binding.publicReposCountView.text = it.public_repos.toString()
                binding.privateReposCountView.text = it.total_private_repos.toString()

            }
        })


        viewModel.reposLiveData.observe(viewLifecycleOwner, {

            if (it != null) {
                var forksCount = 0

                it.forEachIndexed { i, repo ->
                    if (repo.fork) {
                        forksCount += 1
                    }
                }

                binding.forksCountView.text = forksCount.toString()
            }

        })


        viewModel.updateUserData()
        viewModel.updateRepositoriesData()


    }

}