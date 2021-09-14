package com.alexandr7035.gitstat.data.remote

import com.alexandr7035.gitstat.data.remote.model.RepositoryModel
import com.alexandr7035.gitstat.data.remote.model.UserModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface GitHubApi {
    @GET("/user")
    //fun getUser(@Header("Authorization") auth: String, @Path("username") username: String): Call<UserModel>
    suspend fun getUser(@Header("Authorization") auth: String): Response<UserModel>

    // Use search api because "/users/{username}/repos" doesn't return private repos
    //
    // NOTE: This query not returning forks
    // Github support: When a fork has less stars than its parent,
    // it is not indexed at all for code search.
    // It won't show up even when you use fork:true.
    // So fork:true is not used here deliberately
    @GET("/user/repos")
    suspend fun getRepositories(
        @Header("Authorization") auth: String): Response<List<RepositoryModel>>

}




