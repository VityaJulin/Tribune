package com.example.android_krud_app.dto


enum class AttachmentType {
    IMAGE, AUDIO, VIDEO
}

data class AttachmentModel(val id: String, val url: String, val type: AttachmentType)

enum class PostType {
    POST, REPOST
}

data class PostModel(
    val id: Long,
    val source: PostModel? = null,
    val ownerId: Long,
    val ownerName: String,
    val created: Int,
    var content: String? = null,
    var likes: Int = 0,
    var likedByMe: Boolean = false,
    var dislikes: Int = 0,
    var dislikedByMe: Boolean = false,
    val link: String? = null,
    val type: PostType = PostType.POST,
    val attachment: AttachmentModel?
) {
    var likeActionPerforming = false
    var dislikeActionPerforming = false

    fun updateLikes(updatedModel: PostModel) {
        if (id != updatedModel.id) throw IllegalAccessException("Ids are different")
        likes = updatedModel.likes
        likedByMe = updatedModel.likedByMe
    }

    fun updateDislikes(updatedModel: PostModel) {
        if (id != updatedModel.id) throw IllegalAccessException("Ids are different")
        dislikes = updatedModel.dislikes
        dislikedByMe = updatedModel.dislikedByMe
    }


    fun updatePost(updatedModel: PostModel) {
        if (id != updatedModel.id) throw IllegalAccessException("Ids are different")
        likes = updatedModel.likes
        likedByMe = updatedModel.likedByMe
        content = updatedModel.content
        dislikes = updatedModel.dislikes
        dislikedByMe = updatedModel.dislikedByMe
    }
}
