package com.example.echoes.data.network

import android.content.Context
import com.example.echoes.utils.UserPrefManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // Get the token from UserPrefManager
        val token = UserPrefManager.getAuthToken(context)
        if (token != null) {
            // Add Authorization header
            requestBuilder.addHeader("tkn", "$token")
        }

        return chain.proceed(requestBuilder.build())
    }
}
