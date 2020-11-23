package com.example.tribune.sideeffects

import com.example.tribune.FeedAction
import com.example.tribune.FeedSideEffect
import com.example.tribune.mvi.SideEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

object DoubleDislikeSideEffect : SideEffect<FeedAction, FeedSideEffect> {
    override fun transform(actions: Flow<FeedAction>): Flow<FeedSideEffect> =
        actions.filterIsInstance<FeedAction.DoubleDislike>()
            .map { FeedSideEffect.DoubleDislikeError }
}