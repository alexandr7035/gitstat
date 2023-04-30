package by.alexandr7035.gitstat.data.remote

import by.alexandr7035.gitstat.data.local.preferences.AppPreferences
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain

class AuthInterceptor(private val appPreferences: AppPreferences) : HttpInterceptor {
    override suspend fun intercept(request: HttpRequest, chain: HttpInterceptorChain): HttpResponse {
        val newReq = request.newBuilder().addHeader("Authorization", "token ${appPreferences.getToken()}").build()
        return chain.proceed(newReq)
    }
}