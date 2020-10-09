package com.example.tribune.dto

import com.example.android_krud_app.dto.AttachmentModel

data class UserModel(
    val id: Long,
    val username: String,
    var isReadOnly: Boolean = false,
    val badge: String? = null,
    val avatar: AttachmentModel?
)