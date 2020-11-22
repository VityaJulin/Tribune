package com.example.tribune.mvi

import kotlinx.coroutines.flow.Flow

interface SideEffect<A, SE> {
    fun transform(actions: Flow<A>): Flow<SE>
}