package com.example.echoes.presentation.screens

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.echoes.presentation.components.BottomBar
import com.example.echoes.presentation.navigation.Screen
import com.example.echoes.presentation.viewmodel.EchoesViewModel

@Composable
fun MainScreen(
    context: Context,
    viewModel: EchoesViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    remember { mutableStateOf(Screen.Upload.route) }

    val currentScreen = remember { mutableStateOf(Screen.Upload.route) }

    val isLoggedIn = remember { mutableStateOf(true) }

    if (isLoggedIn.value) {
        Scaffold(
            containerColor = Color.White,
            modifier = Modifier.fillMaxWidth(),
            bottomBar = {
                BottomBar(
                    currentScreen = currentScreen.value,
                    onItemSelected = { screen ->
                        currentScreen.value = screen.route
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Upload.route,
                Modifier.padding(innerPadding)
            ) {
                composable(Screen.UploadedArticles.route) {
                    UploadedArticlesScreen(
                        context = context,
                        viewModel = viewModel,
                        onArticleClick = {}
                    )
                }
                composable(Screen.Upload.route) {
                    UploadArticle(viewModel = viewModel, context = context)
                }
                composable(Screen.Profile.route) {
                    ProfileSection(viewModel = viewModel, context = context)
                }
            }
        }
    } else {
        RegistrationScreen { name, email, phone ->

        }
    }
}
