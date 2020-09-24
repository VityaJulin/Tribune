package com.example.tribune.api

import com.example.android_krud_app.dto.PostModel
import retrofit2.Response
import retrofit2.http.*

// Данные для авторизации
data class AuthRequestParams(val username: String, val password: String)

// Токен для идентификации последущих запросов
data class Token(val token: String)

// Данные для регистрации
data class RegistrationRequestParams(val username: String, val password: String)

// Данные для создания поста (для новых постов id=0)
data class CreatePostRequest(val id: Long = 0, val content: String)


// тип поста автоматически определяется на базе sourceId и link
data class PostRequest(
    val id: Long = 0, // 0 - новый, !0 - редактируем существующий, если есть права
    val sourceId: Long? = null, // !null - если репостим
    val content: String? = null,
    val link: String? = null, // например, ссылка на Youtube
    val attachmentId: String? = null // id вложения, если есть
)


interface API {
    // URL запроса (без учета основного адресса)
    @POST("api/v1/authentication")
    suspend fun authenticate(@Body authRequestParams: AuthRequestParams): Response<Token>

    @POST("api/v1/registration")
    suspend fun register(@Body registrationRequestParams: RegistrationRequestParams): Response<Token>

    @POST("api/v1/posts")
    suspend fun createPost(@Body createPostRequest: CreatePostRequest): Response<Void>

    @GET("api/v1/posts/recent")
    suspend fun getPosts(): Response<List<PostModel>>

    @POST("api/v1/posts/{id}/likes")
    suspend fun likedByMe(@Path("id") id: Long): Response<PostModel>

    @DELETE("api/v1/posts/{id}/likes")
    suspend fun cancelMyLike(@Path("id") id: Long): Response<PostModel>

    @POST("api/v1/posts/{id}/reposts")
    suspend fun repostedByMe(@Path("id") id: Long): Response<PostModel>

    @GET("api/v1/posts/recent/after/{id}")
    suspend fun getPostsAfter(@Path("id") id: Long): Response<List<PostModel>>

    @GET("api/v1/posts/recent/before/{id}")
    suspend fun getPostsBefore(@Path("id") id: Long): Response<List<PostModel>>

    @GET("api/v1/posts/recent")
    suspend fun getRecent(): Response<List<PostModel>>

}