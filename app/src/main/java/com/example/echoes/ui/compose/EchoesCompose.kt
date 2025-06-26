package com.example.echoes.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.echoes.R
import com.example.echoes.util.Screen




@Composable
fun AppNavigationBar(
    currentScreen: String,
    onItemSelected: (Screen) -> Unit
) {
    NavigationBar {
        listOf(Screen.Upload, Screen.Profile).forEach { screen ->
            NavigationBarItem(
                selected = currentScreen == screen.route,
                onClick = { onItemSelected(screen) },
                icon = { Icon(imageVector = screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) }
            )
        }
    }
}

@Composable
fun FancyBottomBar(
    currentScreen: String,
    onItemSelected: (Screen) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        // Navigation Bar Background
        NavigationBar(
            containerColor = colorResource(id = R.color.code_F6F6F6),
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            val screens = listOf(Screen.UploadedArticles,Screen.Upload, Screen.Profile)

            screens.forEach { screen ->
                NavigationBarItem(
                    selected = currentScreen == screen.route,
                    onClick = { onItemSelected(screen) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = colorResource(id = R.color.code_F6F6F6)// Removes the oval background
                    ),
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = screen.label,
                            tint = if (currentScreen == screen.route) Color.Black else Color.Gray
                        )
                    },
                    label = {
                        Text(
                            text = screen.label,
                            color = if (currentScreen == screen.route) Color.Black else Color.Gray
                        )
                    }
                )
            }
        }

       /* // Floating Action Button (Upload Article)
        FloatingActionButton(
            onClick = { onItemSelected(Screen.Upload) },
            containerColor = Color(0xFF6200EE),
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.TopCenter)
                .offset(y = -30.dp) // Elevate FAB above the bar
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_clock),
                contentDescription = "Upload Article",
                tint = Color.White
            )
        }*/
    }
}




