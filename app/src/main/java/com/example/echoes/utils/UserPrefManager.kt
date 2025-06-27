package com.example.echoes.utils

import android.content.Context

object UserPrefManager {
    fun saveUserData(context: Context, name: String, email: String, phone: String) {
        val sharedPreferences = context.getSharedPreferences("EchoesApp", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("name", name)
            putString("email", email)
            putString("phone", phone)
            putBoolean("isLoggedIn", true)
            apply()
        }
    }
}