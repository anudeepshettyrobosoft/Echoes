package com.example.echoes.domain.model

import com.example.echoes.R

data class Voucher(
    val id: String,
    val title: String,
    val description: String,
    val pointsRequired: Int,
    val type: String,
    val code: String = "", // This can be shown after redemption,
    var icon: Int = R.drawable.ic_gift // Icon can be set later, e.g., after fetching from server
){
    fun getVendorIcon(): Int {
        return when (type) {
            "Amazon" -> R.drawable.amazon
            "Flipkart" -> R.drawable.flipkart
            "Myntra" -> R.drawable.myntra
            "Uber" -> R.drawable.uber
            "Ola" -> R.drawable.ola
            "Zepto" -> R.drawable.zepto
            else -> R.drawable.ic_gift // Default icon if type is unknown
        }

    }
}