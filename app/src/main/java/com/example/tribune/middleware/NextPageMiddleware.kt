package com.example.tribune.middleware

import com.example.tribune.FeedAction
import com.example.tribune.FeedState
import com.example.tribune.Repository
import com.example.tribune.mvi.Middleware
import kotlinx.coroutines.flow.*

class NextPageMiddleware(
    private val repository: Repository
) : Middleware<FeedAction, FeedState> {

    override fun transform(
        actions: Flow<FeedAction>,
        state: StateFlow<FeedState>,
    ): Flow<FeedAction> =
        actions.filterIsInstance<FeedAction.LoadNextPage>()
            .map { action ->
                if (state.value.lastPageLoaded) {
                    FeedAction.LastPageLoaded
                } else {
                    repository.getPostsBefore(action.lastPageId).let {
                        if (it.isSuccessful) {
                            val posts = it.body().orEmpty()
                            if (posts.isEmpty()) {
                                FeedAction.LastPageLoaded
                            } else {
                                FeedAction.PageLoaded(posts)
                            }
                        } else {
                            FeedAction.NextPageError
                        }
                    }
                }
            }.catch {
                emit(FeedAction.NextPageError)
            }
}