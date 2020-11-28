package com.example.tribune

// Здесь только то, что должно быть вызвано один раз
sealed class FeedSideEffect {
    object RefreshError : FeedSideEffect()
    object DoubleLikeError : FeedSideEffect()
    object DoubleDislikeError : FeedSideEffect()
    object Logout : FeedSideEffect()
}