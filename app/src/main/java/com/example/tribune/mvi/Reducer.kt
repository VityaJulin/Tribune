package com.example.tribune.mvi

interface Reducer<A, S> {
    fun reduce(action: A, currentState: S): S
}