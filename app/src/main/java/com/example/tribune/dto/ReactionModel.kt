package com.example.tribune.dto

enum class ReactionType {
    LIKE, DISLIKE
}

data class ReactionModel (
    val user: UserModel,
    val date: Long,
    val type: ReactionType
)