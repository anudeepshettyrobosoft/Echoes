package com.example.echoes.domain.repository

import android.content.ContentResolver
import android.content.Context
import com.example.echoes.data.model.GenericResponse
import com.example.echoes.data.model.LoginRequest
import com.example.echoes.data.model.LoginResponse
import com.example.echoes.data.model.LogoutResponse
import com.example.echoes.data.model.RegisterUserRequest
import com.example.echoes.data.model.RegisterUserResponse
import com.example.echoes.data.model.UploadNewsRequest
import com.example.echoes.data.model.UploadNewsResponse
import com.example.echoes.domain.model.NewsItem
import com.example.echoes.domain.model.ProfileData
import com.example.echoes.domain.model.Voucher

interface NewsRepository {
    suspend fun uploadNews(
        userId: String,
        newsRequest: UploadNewsRequest,
        contentResolver: ContentResolver,
        context: Context
    ): Result<UploadNewsResponse>

    suspend fun getNewsList(userId:String): Result<List<NewsItem>>

    suspend fun getProfileData(): Result<ProfileData>

    suspend fun registerUser(
      registerUserRequest: RegisterUserRequest
    ): Result<RegisterUserResponse>

    suspend fun login(
        loginRequest: LoginRequest
    ): Result<LoginResponse>

    suspend fun logout(): Result<LogoutResponse>

    suspend fun getVouchersList(): Result<List<Voucher>>

    suspend fun redeemVoucher(
        voucherId: String,
    ): Result<GenericResponse>
}

