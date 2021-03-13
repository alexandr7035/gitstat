package com.example.gitstat

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.gitstat.api.GitHubApi
import com.example.gitstat.databinding.FragmentProfileBinding
import com.example.gitstat.databinding.FragmentReposBinding
import com.example.gitstat.model.RepositoryModel
import com.example.gitstat.model.UserModel
import com.squareup.picasso.Picasso
import okhttp3.Credentials
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ReposFragment : Fragment() {

    private val LOG_TAG = "DEBUG_TAG"
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var reposList: ArrayList<RepositoryModel>
    private lateinit var userModel: UserModel

    private lateinit var binding: FragmentReposBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReposBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Shared pref
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)

        val user = sharedPreferences.getString(getString(R.string.shared_pref_login), "NONE")
        val token = sharedPreferences.getString(getString(R.string.shared_pref_token), "NONE")
        //Toast.makeText(requireActivity(), "Auth '$user' with token '$token'", Toast.LENGTH_LONG).show()
        Log.d(LOG_TAG, "Auth '$user' with token '$token'")

        var authCredentials = Credentials.basic(user, token)


        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val gitHubApi: GitHubApi = retrofit.create(GitHubApi::class.java)

        val reposCall: Call<List<RepositoryModel>> = gitHubApi.getRepositories(authCredentials, "$user")

        reposCall.enqueue(object : Callback<List<RepositoryModel>> {
            override fun onResponse(call: Call<List<RepositoryModel>>, response: Response<List<RepositoryModel>>) {
                if (response.isSuccessful) {
                    reposList = response.body() as ArrayList<RepositoryModel>

                    var forksCount = 0
                    reposList.forEachIndexed { i, repo ->
                        if (repo.fork) {
                            forksCount += 1
                        }
                    }

                    binding.forksCountView.text = forksCount.toString()
                    
                    Log.d(LOG_TAG, "REPOS LIST $reposList")
                }
                else {
                    Log.d(LOG_TAG, "repos request not successfull")

                    val data = response.errorBody()!!.string()
                    try {
                        val jObjError = JSONObject(data)
                        Toast.makeText(
                            context, jObjError.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<RepositoryModel>>, t: Throwable) {
                Log.d(LOG_TAG, "repos failed")
            }

        })


        val userCall: Call<UserModel> = gitHubApi.getUser(authCredentials, "$user")

        userCall.enqueue(object : Callback<UserModel> {

            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {

                Log.d(LOG_TAG, response.body().toString())

                if (response.isSuccessful) {
                    //Log.d(LOG_TAG, "SUCCESS RESPONSE")

                    // FixMe
                    userModel = response.body()!!

                    binding.publicReposCountView.text = userModel.public_repos.toString()
                    binding.privateReposCountView.text = userModel.total_private_repos.toString()

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