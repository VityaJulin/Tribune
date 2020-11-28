package com.example.tribune.middleware

import com.example.tribune.FeedAction
import com.example.tribune.FeedState
import com.example.tribune.Repository
import com.example.tribune.mvi.Middleware
import kotlinx.coroutines.flow.*

class LikeMiddleware(
    private val repository: Repository
) : Middleware<FeedAction, FeedState> {

    override fun transform(
        actions: Flow<FeedAction>,
        state: StateFlow<FeedState>,
    ): Flow<FeedAction> =
        actions.filterIsInstance<FeedAction.Like>()
            .map { action ->
                val post = state.value.posts.getOrNull(
                    action.position
                ) ?: return@map FeedAction.LikeError()
                if (post.likedByMe) {
                    return@map FeedAction.DoubleLike
                }
                repository.likedByMe(post.id).handleLogout(
                    onSuccess = { FeedAction.LikeLoaded(requireNotNull(it.body())) },
                    onFailure = { FeedAction.LikeError(post.id) }
                )
            }.catch {
                emit(FeedAction.LikeError())
            }
}