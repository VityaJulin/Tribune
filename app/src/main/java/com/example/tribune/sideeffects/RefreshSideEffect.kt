package com.example.tribune.sideeffects

import com.example.tribune.FeedAction
import com.example.tribune.FeedSideEffect
import com.example.tribune.mvi.SideEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

object RefreshSideEffect : SideEffect<FeedAction, FeedSideEffect> {
    override fun transform(actions: Flow<FeedAction>): Flow<FeedSideEffect> =
        actions.filterIsInstance<FeedAction.RefreshError>()
            .map { FeedSideEffect.RefreshError }
}