package com.example.echoes.utils

object AppManager {
   //const val BASE_URL = "https://shining-freely-bison.ngrok-free.app/echoes/"
    const val BASE_URL = "http://10.0.2.2:8080/echoes/"
    const val PROFILE_IMAGE_URL = "${BASE_URL}user-image/"
    const val NEWS_IMAGE_URL = "$BASE_URL + news-image/"
    val Categories = listOf(
        "World",
        "Politics",
        "Business/Finance",
        "Sports",
        "Crime",
        "Weather",
        "Health",
        "Technology",
        "Real Estate",
        "Events",
        "Law",
        "Entertainment",
        "Local"
    )
}