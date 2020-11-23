package com.example.tribune.middleware

import com.example.tribune.FeedAction
import com.example.tribune.FeedState
import com.example.tribune.Repository
import com.example.tribune.mvi.Middleware
import kotlinx.coroutines.flow.*

class RefreshMiddleware(
    private val repository: Repository
) : Middleware<FeedAction, FeedState> {

    override fun transform(
        actions: Flow<FeedAction>,
        state: StateFlow<FeedState>,
    ): Flow<FeedAction> =
        actions.filterIsInstance<FeedAction.Refresh>()
            .map {
                repository.getRecent().let {
                    if (it.isSuccessful) {
                        FeedAction.FirstPageLoaded(it.body().orEmpty())
                    } else {
                        FeedAction.RefreshError
                    }
                }
            }.catch {
                emit(FeedAction.RefreshError)
            }
}