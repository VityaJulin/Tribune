package com.example.tribune.middleware

import com.example.tribune.FeedAction
import com.example.tribune.FeedState
import com.example.tribune.Repository
import com.example.tribune.mvi.Middleware
import kotlinx.coroutines.flow.*

class RetryPageMiddleware(
    private val repository: Repository,
) : Middleware<FeedAction, FeedState> {

    @Suppress("EXPERIMENTAL_API_USAGE")
    override fun transform(
        actions: Flow<FeedAction>,
        state: StateFlow<FeedState>,
    ): Flow<FeedAction> =
        actions.filterIsInstance<FeedAction.RetryNextPage>()
            .mapLatest { action ->
                repository.getPostsAfter(action.lastPageId).handleLogout(
                    onSuccess = { FeedAction.PageLoaded(it.body().orEmpty()) },
                    onFailure = { FeedAction.NextPageError }
                )
            }.catch {
                emit(FeedAction.NextPageError)
            }
}