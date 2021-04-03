package com.example.gitstat.api

import com.example.gitstat.model.ReposSearchModel
import com.example.gitstat.model.RepositoryModel
import com.example.gitstat.model.UserModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {
    @GET("/users/{username}")
    fun getUser(@Header("Authorization") auth: String, @Path("username") username: String): Call<UserModel>

    //@GET("/users/{username}/repos")
    @GET("/search/repositories")
    fun getRepositories(
        @Header("Authorization") auth: String,
        @Query("q") userParam: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("fork") showForks: Boolean): Call<ReposSearchModel>
}




