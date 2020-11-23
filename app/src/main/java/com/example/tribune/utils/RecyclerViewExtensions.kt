package com.example.tribune.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

inline fun RecyclerView.doOnScrolledToBottom(crossinline action: () -> Unit) {
    require(layoutManager is LinearLayoutManager)

    addOnScrollListener(
        object : RecyclerView.OnScrollListener() {

            private val linearLayoutManager = layoutManager as LinearLayoutManager

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                with(linearLayoutManager) {
                    val visibleItemCount = childCount
                    val firstVisibleItemPosition = findFirstVisibleItemPosition()
                    val totalItemCount = itemCount

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                        action()
                    }
                }
            }
        })
}