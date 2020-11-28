package com.example.tribune

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_krud_app.dto.PostModel
import com.example.tribune.activity.AuthorPage
import com.example.tribune.activity.Statistic
import com.example.tribune.adapter.FeedModel
import com.example.tribune.adapter.PostAdapter
import com.example.tribune.middleware.*
import com.example.tribune.mvi.Store
import com.example.tribune.sideeffects.DoubleDislikeSideEffect
import com.example.tribune.sideeffects.DoubleLikeSideEffect
import com.example.tribune.sideeffects.LogoutSideEffect
import com.example.tribune.sideeffects.RefreshSideEffect
import com.example.tribune.utils.doOnScrolledToBottom
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.installations.FirebaseInstallations
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.toast.toast

class FeedActivity : AppCompatActivity(R.layout.activity_feed),
    PostAdapter.OnLikeBtnClickListener, PostAdapter.OnDislikeBtnClickListener,
    PostAdapter.OnAvatarBtnClickListener, PostAdapter.OnStatisticBtnClicklistener {

    private val adapter = PostAdapter(
        likeBtnClickListener = this,
        dislikeBtnClickListener = this,
        avatarBtnClickListener = this,
        statisticBtnClickListener = this,
        retryPageClickListener = {
            feedStore.state.value.posts.lastOrNull()?.id?.let {
                feedStore.accept(FeedAction.RetryNextPage(it))
            }
        }
    )

    // Бизнес логика
    private val feedStore = Store(
        reducer = FeedReducer,
        initialState = FeedState(),
        middlewares = listOf(
            FirstPageMiddleware(Repository),
            NextPageMiddleware(Repository),
            RefreshMiddleware(Repository),
            RetryPageMiddleware(Repository),
            LikeMiddleware(Repository),
            DislikeMiddleware(Repository)
        ),
        sideEffects = listOf(
            RefreshSideEffect,
            DoubleLikeSideEffect,
            DoubleDislikeSideEffect,
            LogoutSideEffect,
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        container.adapter = adapter
        container.layoutManager = LinearLayoutManager(this)
        fab.setOnClickListener {
            start<CreatePostActivity>()
        }
        swipeContainer.setOnRefreshListener {
            feedStore.accept(FeedAction.Refresh)
        }
        requestToken()

        lifecycleScope.launch {
            feedStore.wire()
        }

        feedStore.state.onEach { state ->
            swipeContainer.isRefreshing = state.refreshing
            val models = state.posts.map {
                FeedModel.Post(it)
            }
            adapter.submitList(
                when {
                    state.nextPageLoading -> models + FeedModel.Progress
                    state.nextPageError -> models + FeedModel.Error
                    else -> models
                }
            )
            empty_page_message.isVisible = state.emptyPage
            progress_horizontal.isVisible = state.emptyPageLoading
            retry_btn.isVisible = state.emptyPageError
            retry_btn.setOnClickListener {
                feedStore.accept(FeedAction.LoadFirstPage)
            }
        }.launchIn(lifecycleScope)

        feedStore.sideEffect.onEach {
            when (it) {
                FeedSideEffect.RefreshError -> toast(R.string.refresh_error)
                FeedSideEffect.DoubleLikeError -> toast(R.string.error_double_vote)
                FeedSideEffect.DoubleDislikeError -> toast(R.string.error_double_vote)
                FeedSideEffect.Logout -> {
                    getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).edit {
                        remove(AUTHENTICATED_SHARED_KEY)
                    }
                    start<MainActivity> {
                        addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        )
                    }
                }
            }
        }.launchIn(lifecycleScope)

        container.doOnScrolledToBottom {
            feedStore.state.value.posts.lastOrNull()?.id?.let {
                feedStore.accept(FeedAction.LoadNextPage(it))
            }
        }
        feedStore.accept(FeedAction.LoadFirstPage)
    }

    override fun onRestart() {
        super.onRestart()
        feedStore.accept(FeedAction.Refresh)
    }

    override fun onLikeBtnClicked(item: PostModel, position: Int) {
        feedStore.accept(FeedAction.Like(position))
    }

    override fun onDislikeBtnClicked(item: PostModel, position: Int) {
        feedStore.accept(FeedAction.Dislike(position))
    }

    override fun onAvatarBtnClicked(item: PostModel, position: Int) {
        val intent = Intent(this@FeedActivity, AuthorPage::class.java)
        intent.putExtra(USER_ID, item.ownerId)
        startActivity(intent)
    }

    override fun onStatisticBtnCliked(item: PostModel, position: Int) {
        val intent = Intent(this@FeedActivity, Statistic::class.java)
        intent.putExtra(USER_ID, item.ownerId)
        startActivity(intent)
    }

    private fun requestToken() {
        with(GoogleApiAvailability.getInstance()) {
            val code = isGooglePlayServicesAvailable(this@FeedActivity)
            if (code == ConnectionResult.SUCCESS) {
                return@with
            }

            if (isUserResolvableError(code)) {
                getErrorDialog(this@FeedActivity, code, 9000).show()
                return
            }

            feed.longSnackbar(getString(R.string.google_play_unavailable))
            return
        }

        FirebaseInstallations.getInstance().getToken(false).addOnSuccessListener {
            lifecycleScope.launch {
                Repository.registerPushToken(it.token)
            }
        }
    }
}
