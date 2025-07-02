package com.example.echoes.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    @SerializedName("pwd")
    val password:String
)