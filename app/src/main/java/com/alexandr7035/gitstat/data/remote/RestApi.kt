package com.alexandr7035.gitstat.data.remote

import com.alexandr7035.gitstat.data.remote.model.RepositoryModel
import com.alexandr7035.gitstat.data.remote.model.UserModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RestApi {
    @GET("/user")
    suspend fun getUser(@Header("Authorization") auth: String): Response<UserModel>

    @GET("/user/repos")
    suspend fun getRepositories(
        @Header("Authorization") auth: String,
        @Query("per_page") per_page: Int): Response<List<RepositoryModel>>

}




