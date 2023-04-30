package by.alexandr7035.gitstat.core.extensions

import by.alexandr7035.gitstat.core.AppError
import by.alexandr7035.gitstat.core.ErrorType
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Query


// Takes Apollo Query as parameter
// Returns directly query.data
suspend fun <D : Query.Data> ApolloClient.performRequestWithDataResult(query: Query<D>): D {
    val response = this.query(query).execute()

    if (response.hasErrors()) {
        throw AppError(ErrorType.UNKNOWN_ERROR)
    }

    else {
        return response.data ?: throw AppError(ErrorType.UNKNOWN_ERROR)
    }
}

