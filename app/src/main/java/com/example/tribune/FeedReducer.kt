package com.example.tribune

import com.example.android_krud_app.dto.PostModel
import com.example.tribune.mvi.Reducer

object FeedReducer : Reducer<FeedAction, FeedState> {
    override fun reduce(action: FeedAction, currentState: FeedState): FeedState =
        when (action) {
            FeedAction.LoadFirstPage -> FeedState(emptyPageLoading = true)
            is FeedAction.FirstPageLoaded -> FeedState(action.posts)
            is FeedAction.LoadNextPage -> {
                if(currentState.lastPageLoaded) {
                    currentState
                } else {
                    currentState.copy(nextPageLoading = true, nextPageError = false)
                }
            }
            FeedAction.Refresh -> {
                currentState.copy(refreshing = true, nextPageLoading = false)
            }
            is FeedAction.RetryNextPage -> {
                currentState.copy(nextPageLoading = true, nextPageError = false)
            }
            FeedAction.NextPageError -> {
                currentState.copy(nextPageLoading = false, nextPageError = true)
            }
            FeedAction.LastPageLoaded -> {
                currentState.copy(lastPageLoaded = true, nextPageLoading = false)
            }
            is FeedAction.PageLoaded -> {
                currentState.copy(
                    posts = currentState.posts + action.posts,
                    nextPageLoading = false
                )
            }
            FeedAction.FirstPageError -> FeedState(emptyPageError = true)
            is FeedAction.LikeLoaded -> {
                reduceLiked(currentState, action.post)
            }
            is FeedAction.DislikeLoaded -> {
                reduceLiked(currentState, action.post)
            }
            is FeedAction.Like -> {
                currentState.copy(
                    posts = currentState.posts.toMutableList().apply {
                        get(action.position).likeActionPerforming = true
                    }
                )
            }
            is FeedAction.Dislike -> {
                currentState.copy(
                    posts = currentState.posts.toMutableList().apply {
                        get(action.position).dislikeActionPerforming = true
                    }
                )
            }
            is FeedAction.LikeError -> {
                currentState.copy(
                    posts = currentState.posts.toMutableList().apply {
                        find { it.id == action.id }?.likeActionPerforming = false
                    }
                )
            }
            is FeedAction.DislikeError -> {
                currentState.copy(
                    posts = currentState.posts.toMutableList().apply {
                        find { it.id == action.id }?.dislikeActionPerforming = false
                    }
                )
            }
            FeedAction.EmptyPage -> FeedState(emptyPage = true)
            FeedAction.RefreshError -> currentState.copy(refreshing = false)
            FeedAction.Logout,
            FeedAction.DoubleLike,
            FeedAction.DoubleDislike -> currentState
        }

    private fun reduceLiked(currentState: FeedState, post: PostModel): FeedState =
        currentState.copy(
            posts = currentState.posts.map {
                if (it.id == post.id) {
                    post
                } else {
                    it
                }
            }
        )
}