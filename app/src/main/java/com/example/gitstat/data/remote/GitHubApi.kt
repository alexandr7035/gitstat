package com.example.gitstat.data.remote

import com.example.gitstat.data.remote.ReposSearchModel
import com.example.gitstat.data.remote.UserModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {
    @GET("/users/{username}")
    //fun getUser(@Header("Authorization") auth: String, @Path("username") username: String): Call<UserModel>
    suspend fun getUser(@Header("Authorization") auth: String, @Path("username") username: String): Response<UserModel>

    // Use search api because "/users/{username}/repos" doesn't return private repos
    //
    // NOTE: This query not returning forks
    // Github support: When a fork has less stars than its parent,
    // it is not indexed at all for code search.
    // It won't show up even when you use fork:true.
    // So fork:true is not used here deliberately
    @GET("/search/repositories")
    suspend fun getRepositories(
        @Header("Authorization") auth: String,
        @Query("q") userParam: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int): Response<ReposSearchModel>

}




