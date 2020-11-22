package com.example.tribune.adapter

import com.example.android_krud_app.dto.PostModel

sealed class FeedModel {
    open fun areItemsTheSame(other: FeedModel): Boolean = this == other
    data class Post(val post: PostModel): FeedModel() {
        override fun areItemsTheSame(other: FeedModel): Boolean =
            other is Post && other.post.id == post.id
    }
    object Error: FeedModel()
    object Progress: FeedModel()
}