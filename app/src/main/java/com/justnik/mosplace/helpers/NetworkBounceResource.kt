package com.justnik.mosplace.helpers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

fun <ResultType, RequestType>networkBounceResource(
    query: () -> Flow<ResultType>,
    request: suspend () -> RequestType,
    saveFetchResult: suspend (RequestType) -> Unit,
    shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = query().first()

    if (shouldFetch(data)){
        emit(data)
    }
}