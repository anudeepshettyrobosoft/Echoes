package com.example.echoes.network

import com.example.echoes.network.model.NewsItem
import com.example.echoes.network.model.ProfileData
import com.example.echoes.network.model.UploadNewsRequest
import com.example.echoes.network.model.UploadNewsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface NewsApiService {

    @Multipart
    @POST("submit-news/1")
    suspend fun uploadNews(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("category") category: RequestBody,
        @Part("location") location: RequestBody?,
        @Part("date") date: RequestBody?,
        @Part("time") time: RequestBody?,
        @Part imageORVideo : MultipartBody.Part? // List to handle multiple images
    ): Response<UploadNewsResponse>

    @GET("user-news/1")
    suspend fun getNewsList(): Response<List<NewsItem>>

    @GET("user-info/1")
    suspend fun getProfileData(): Response<ProfileData>
}