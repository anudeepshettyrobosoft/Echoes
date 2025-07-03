package com.example.echoes.data.network

import com.example.echoes.data.model.GenericResponse
import com.example.echoes.data.model.LoginRequest
import com.example.echoes.data.model.LoginResponse
import com.example.echoes.data.model.LogoutResponse
import com.example.echoes.data.model.RegisterUserResponse
import com.example.echoes.data.model.UploadNewsResponse
import com.example.echoes.domain.model.NewsItem
import com.example.echoes.domain.model.ProfileData
import com.example.echoes.domain.model.Voucher
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface NewsApiService {

    @Multipart
    @POST("submit-news/{userId}")
    suspend fun uploadNews(
        @Path("userId") userId: String,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("category") category: RequestBody,
        @Part("location") location: RequestBody?,
        @Part("date") date: RequestBody?,
        @Part("time") time: RequestBody?,
        @Part imageORVideo: MultipartBody.Part? // List to handle multiple images
    ): Response<UploadNewsResponse>

    @GET("user-news/{userId}")
    suspend fun getNewsList(
        @Path("userId") userId: String
    ): Response<List<NewsItem>>

    @GET("user-info")
    suspend fun getProfileData(): Response<ProfileData>

    @Multipart
    @POST("register-user")
    suspend fun registerUser(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("type") type: RequestBody,
    ): Response<RegisterUserResponse>

    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @POST("logout")
    suspend fun logout(
    ): Response<LogoutResponse>

    @GET("voucher/list")
    suspend fun getVouchers(
    ): Response<List<Voucher>>

    @GET("voucher/redeem/{voucherId}")
    suspend fun redeemVoucher(
        @Path("voucherId") voucherId: String
    ): Response<GenericResponse>

}