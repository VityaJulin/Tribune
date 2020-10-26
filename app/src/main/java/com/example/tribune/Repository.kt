package com.example.tribune

import android.graphics.Bitmap
import com.example.android_krud_app.dto.AttachmentModel
import com.example.tribune.api.API
import com.example.tribune.api.AuthRequestParams
import com.example.tribune.api.CreatePostRequest
import com.example.tribune.api.RegistrationRequestParams
import com.example.tribune.api.interceptor.InjectAuthTokenInterceptor
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream


object Repository {

    private var retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl("https://android-krud-api.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    // Добавление interceptor-ов в retrofit клиент. Во все последующие запросы будет добавляться токен
    // и они будут логироваться
    fun createRetrofitWithAuth(authToken: String) {
        val httpLoggerInterceptor = HttpLoggingInterceptor()
        // Указываем, что хотим логировать тело запроса.
        httpLoggerInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(InjectAuthTokenInterceptor(authToken))
            .addInterceptor(httpLoggerInterceptor)
            .build()
        retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("https://android-krud-api.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //создаем API на основе нового retrofit-клиента
        API = retrofit.create(com.example.tribune.api.API::class.java)
    }

    // Ленивое создание API
    private var API: API =
        retrofit.create(com.example.tribune.api.API::class.java)


    suspend fun authenticate(login: String, password: String) = API.authenticate(
        AuthRequestParams(login, password)
    )

    suspend fun register(login: String, password: String) =
        API.register(
            RegistrationRequestParams(
                login,
                password
            )
        )

    suspend fun createPost(content: String) = API.createPost(CreatePostRequest(content = content))

    suspend fun getPosts() = API.getPosts()

    suspend fun likedByMe(id: Long) = API.likedByMe(id)

    suspend fun cancelMyLike(id: Long) = API.cancelMyLike(id)

    suspend fun repostedByMe(id: Long) = API.repostedByMe(id)

    suspend fun getPostsAfter(id: Long) = API.getPostsAfter(id)

    suspend fun getPostsBefore(id: Long) = API.getPostsBefore(id)

    suspend fun getRecent() = API.getRecent()

    suspend fun dislikedByMe(id: Long) = API.dislikedByMe(id)

    suspend fun getPostsByUserId(userId: Long) = API.getPostsByUserId(userId)

    suspend fun getUserById(userId: Long) = API.getUserById(userId)

    suspend fun upload(bitmap: Bitmap): Response<AttachmentModel> {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val reqFIle =
            RequestBody.create(MediaType.parse("image/jpeg"), bos.toByteArray())
        val body =
            MultipartBody.Part.createFormData("file", "image.jpg", reqFIle)
        return API.uploadImage(body)
    }

    suspend fun getReactionsById(postId: Long) = API.getReactionsById(postId)

}
