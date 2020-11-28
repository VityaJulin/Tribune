package com.example.tribune

import com.example.android_krud_app.dto.PostModel

// Это состояние экрана со списком постов.
// Описываем в виде отдельной модели потому что очень много разных вариантов
data class FeedState(
    val posts: List<PostModel> = emptyList(),
    val refreshing: Boolean = false,
    val nextPageLoading: Boolean = false,
    val emptyPageLoading: Boolean = false,
    val emptyPageError: Boolean = false,
    val emptyPage: Boolean = false,
    val nextPageError: Boolean = false,
    val lastPageLoaded: Boolean = false,
)