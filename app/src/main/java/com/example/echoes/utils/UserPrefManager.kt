package com.example.echoes.utils

import android.content.Context

object UserPrefManager {

    private const val PREF_NAME = "EchoesApp"
    private const val TOKEN_KEY = "auth_token"

    fun saveUserData(context: Context, name: String, email: String, phone: String) {
        val sharedPreferences = context.getSharedPreferences("EchoesApp", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("name", name)
            putString("email", email)
            putString("phone", phone)
           // putBoolean("isLoggedIn", true)
            apply()
        }
    }

    fun setUserId(context: Context, userId: String) {
        val sharedPreferences = context.getSharedPreferences("EchoesApp", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("id", userId)
            apply()
        }
    }

    fun getUserId(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("EchoesApp", Context.MODE_PRIVATE)
        return sharedPreferences.getString("id", null)
    }

    fun setUserRegistered(context: Context, isRegistered: Boolean) {
        val sharedPreferences = context.getSharedPreferences("EchoesApp", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("isRegistered", isRegistered)
            apply()
        }
    }

    fun setUserLoggedIn(context: Context, isLoggedIn: Boolean) {
        val sharedPreferences = context.getSharedPreferences("EchoesApp", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", isLoggedIn)
            apply()
        }
    }

    fun isUserLoggedIn(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("EchoesApp", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    fun isUserRegistered(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("EchoesApp", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isRegistered", false)
    }

    fun clearUserData(context: Context) {
        val sharedPreferences = context.getSharedPreferences("EchoesApp", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }

    fun saveAuthToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getAuthToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(TOKEN_KEY, null)
    }
}