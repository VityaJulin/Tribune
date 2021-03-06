package com.example.tribune.api

import com.example.android_krud_app.dto.AttachmentModel
import com.example.android_krud_app.dto.PostModel
import com.example.tribune.dto.PushRequestParamsDto
import com.example.tribune.dto.ReactionModel
import com.example.tribune.dto.UserModel
import okhttp3.MultipartBody
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

    @GET("api/v1/posts/after/{id}")
    suspend fun getPostsAfter(@Path("id") id: Long): Response<List<PostModel>>

    @GET("api/v1/posts/before/{id}")
    suspend fun getPostsBefore(@Path("id") id: Long): Response<List<PostModel>>

    @GET("api/v1/posts/recent")
    suspend fun getRecent(): Response<List<PostModel>>

    @POST("api/v1/posts/{id}/dislikes")
    suspend fun dislikedByMe(@Path("id") id: Long): Response<PostModel>

    @GET("api/v1/posts/user/{id}")
    suspend fun getPostsByUserId(@Path("id") userId: Long): Response<List<PostModel>>

    @GET("api/v1/me/users/{id}")
    suspend fun getUserById(@Path("id") userId: Long): Response<UserModel>

    @Multipart
    @POST("api/v1/me/media")
    suspend fun uploadImage(@Part file: MultipartBody.Part): Response<AttachmentModel>

    @GET("api/v1/posts/reactions/{id}")
    suspend fun getReactionsById(@Path("id") id: Long): Response<List<ReactionModel>>

    @POST("api/v1/token")
    suspend fun registerPushToken(@Body params: PushRequestParamsDto): Response<Void>
}