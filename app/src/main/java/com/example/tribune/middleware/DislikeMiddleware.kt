package com.example.tribune.middleware

import com.example.tribune.FeedAction
import com.example.tribune.FeedState
import com.example.tribune.Repository
import com.example.tribune.mvi.Middleware
import kotlinx.coroutines.flow.*

class DislikeMiddleware(
    private val repository: Repository
) : Middleware<FeedAction, FeedState> {

    override fun transform(
        actions: Flow<FeedAction>,
        state: StateFlow<FeedState>,
    ): Flow<FeedAction> =
        actions.filterIsInstance<FeedAction.Dislike>()
            .map { action ->
                val post = state.value.posts.getOrNull(
                    action.position
                ) ?: return@map FeedAction.DislikeError()
                if (post.likedByMe) {
                    return@map FeedAction.DoubleLike
                }
                repository.dislikedByMe(post.id).handleLogout(
                    onSuccess = { FeedAction.DislikeLoaded(requireNotNull(it.body())) },
                    onFailure = { FeedAction.DislikeError(post.id) }
                )
            }.catch {
                emit(FeedAction.DislikeError())
            }
}