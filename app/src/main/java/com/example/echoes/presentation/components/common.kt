package com.example.echoes.presentation.components

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.echoes.R

val defaultFontFamily = FontFamily(
    Font(R.font.poppins_bold, FontWeight.Bold),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_light, FontWeight.Light)
)

val defaultTextStyle = TextStyle(
    fontFamily = defaultFontFamily,  // This is your font family (Poppins)
    fontWeight = FontWeight.Normal,  // Default weight
    fontSize = 12.sp           // Default font size
)