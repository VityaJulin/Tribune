package com.example.tribune

import com.example.android_krud_app.dto.PostModel

// Здесь указываем все входные данные
sealed class FeedAction {
    // Пользовательские действия
    object LoadFirstPage: FeedAction()
    object Refresh: FeedAction()
    data class LoadNextPage(val lastPageId: Long): FeedAction()
    data class Like(val position: Int): FeedAction()
    data class Dislike(val position: Int): FeedAction()
    data class RetryNextPage(val lastPageId: Long): FeedAction()
    // Ответы от сервера
    object NextPageError: FeedAction()
    object FirstPageError: FeedAction()
    object RefreshError: FeedAction()
    data class LikeError(val id: Long? = null): FeedAction()
    data class DislikeError(val id: Long? = null): FeedAction()
    data class LikeLoaded(val post: PostModel): FeedAction()
    data class DislikeLoaded(val post: PostModel): FeedAction()
    object LastPageLoaded: FeedAction()
    data class FirstPageLoaded(val posts: List<PostModel>): FeedAction()
    data class PageLoaded(val posts: List<PostModel>): FeedAction()
    // Валидация
    object DoubleLike: FeedAction()
    object DoubleDislike: FeedAction()
}