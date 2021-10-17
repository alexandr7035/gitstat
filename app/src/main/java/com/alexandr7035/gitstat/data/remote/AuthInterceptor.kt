package com.alexandr7035.gitstat.data.remote

import com.alexandr7035.gitstat.core.AppPreferences
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.api.http.withHeader
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain

class AuthInterceptor(private val appPreferences: AppPreferences): HttpInterceptor {
    override suspend fun intercept(request: HttpRequest, chain: HttpInterceptorChain): HttpResponse {
        return chain.proceed(request.withHeader("Authorization", "token ${appPreferences.token}"))
    }
}