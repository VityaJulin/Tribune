package com.example.tribune.middleware

import com.example.tribune.FeedAction
import retrofit2.Response

inline fun <T> Response<T>.handleLogout(
    onSuccess: (Response<T>) -> FeedAction,
    onFailure: (Response<T>) -> FeedAction
): FeedAction =
    when {
        isSuccessful -> onSuccess(this)
        code() == 401 -> FeedAction.Logout
        else -> onFailure(this)
    }