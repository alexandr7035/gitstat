package com.alexandr7035.gitstat.data.remote

import com.alexandr7035.gitstat.core.AppPreferences
import com.alexandr7035.gitstat.data.remote.model.RepositoryModel
import com.alexandr7035.gitstat.data.remote.model.UserModel
import retrofit2.Response
import javax.inject.Inject


class RestApiHelper @Inject constructor(private val appPrefs: AppPreferences, private val restApi: RestApi) {

    private val REPOS_PER_PAGE = 1000

    suspend fun getUserData(): Response<UserModel> {
        return restApi.getUser("token ${appPrefs.token}")
    }

    suspend fun getRepositoriesData(): Response<List<RepositoryModel>> {
        return restApi.getRepositories(auth = "token ${appPrefs.token}", per_page = REPOS_PER_PAGE)
    }


    suspend fun loginRequest(token: String): Response<UserModel> {
        return restApi.getUser("token ${token}")
    }

}