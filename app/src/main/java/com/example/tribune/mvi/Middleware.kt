package com.example.tribune.mvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface Middleware<A, S> {
    fun transform(actions: Flow<A>, state: StateFlow<S>): Flow<A>
}