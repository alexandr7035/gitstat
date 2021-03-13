package com.example.gitstat.api

import com.example.gitstat.model.RepositoryModel
import com.example.gitstat.model.UserModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GitHubApi {
    @GET("/users/{username}")
    fun getUser(@Header("Authorization") auth: String, @Path("username") username: String): Call<UserModel>

    @GET("/users/{username}/repos")
    fun getRepositories(
        @Header("Authorization") auth: String,
        @Path("username") username: String): Call<List<RepositoryModel>>
}




