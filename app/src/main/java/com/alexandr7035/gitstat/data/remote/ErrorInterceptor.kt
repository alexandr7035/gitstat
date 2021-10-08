package com.alexandr7035.gitstat.data.remote

import android.util.Log
import com.alexandr7035.gitstat.core.AppError
import com.alexandr7035.gitstat.core.ErrorType
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.exception.ApolloException
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain

class ErrorInterceptor: HttpInterceptor {
    override suspend fun intercept(request: HttpRequest, chain: HttpInterceptorChain): HttpResponse {

        Log.d("DEBUG_TAG", "proceed request")

        val res = try {
            chain.proceed(request)
        }

        // TODO handle subcases if worth
        // Apollo errors are inhereted from RuntimeException
        catch (e: ApolloException) {
            throw AppError(ErrorType.FAILED_CONNECTION)
        }

        if (res.statusCode != 200) {
            when (res.statusCode) {
                401 -> throw AppError(ErrorType.FAILED_AUTHORIZATION)
                else -> throw AppError(ErrorType.UNKNOWN_ERROR)
            }
        }

        return res
    }
}