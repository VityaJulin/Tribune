package com.example.tribune.mvi

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*

class Store<A, S, SE>(
    private val reducer: Reducer<A, S>,
    private val middlewares: List<Middleware<A, S>> = emptyList(),
    private val sideEffects: List<SideEffect<A, SE>> = emptyList(),
    initialState: S,
) {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S>
        get() = _state
    private val _sideEffect = MutableSharedFlow<SE>(extraBufferCapacity = 64)
    val sideEffect: Flow<SE>
        get() = _sideEffect
    private val actionFlow = MutableSharedFlow<A>(extraBufferCapacity = 64)

    fun accept(action: A) {
        actionFlow.tryEmit(action)
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    suspend fun wire() = coroutineScope {
        async {
            actionFlow.map {
                reducer.reduce(it, _state.value)
            }
                .distinctUntilChanged()
                .collect {
                    _state.tryEmit(it)
                }
        }.start()

        async {
            sideEffects.map {
                it.transform(actionFlow)
            }
                .merge()
                .collect {
                    _sideEffect.tryEmit(it)
                }
        }.start()

        async {
            middlewares.map {
                it.transform(actionFlow, _state)
            }
                .merge()
                .collect {
                    actionFlow.tryEmit(it)
                }
        }.start()
    }
}