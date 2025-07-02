package com.example.echoes.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val success: String,
    @SerializedName("message")
    val token: String,
)