package com.example.echoes.data.model

data class RegisterUserRequest(
    val name: String,
    val email: String,
    val phone: String,
    val type: String? = "CLIENT",
)