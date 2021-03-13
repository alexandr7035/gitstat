package com.example.gitstat

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gitstat.api.GitHubApi
import com.example.gitstat.databinding.FragmentProfileBinding
import com.example.gitstat.model.UserModel
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


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

        //val user = sharedPreferences.getString(getString(R.string.shared_pref_login), "NONE")
        //val token = sharedPreferences.getString(getString(R.string.shared_pref_token), "NONE")
        //Toast.makeText(requireActivity(), "Auth '$user' with token '$token'", Toast.LENGTH_LONG).show()

        // Fixme
        // Debug
        val user = "alexandr7035"

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val gitHubApi: GitHubApi = retrofit.create(GitHubApi::class.java)
        val call: Call<UserModel> = gitHubApi.getUser(user)

        call.enqueue(object : Callback<UserModel> {

            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {

                Log.d(LOG_TAG, response.body().toString())

                if (response.isSuccessful) {
                    //Log.d(LOG_TAG, "SUCCESS RESPONSE")



                    // FixMe
                    val userModel: UserModel = response.body()!!

                    binding.userIdView.text = "@${userModel.login}"
                    binding.nameView.text = userModel.name

                    Picasso.get().load(userModel.avatar_url).into(binding.profileImageView)

                    // Fixme
                    // Count also private
                    binding.reposCountView.text = userModel.public_repos.toString()

                }
                else {
                    Log.d(LOG_TAG, "NOT SUCCESSFULL")
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                TODO("Not yet implemented")
            }


        })
    }

}