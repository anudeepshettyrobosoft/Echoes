package com.example.echoes.presentation.screens


import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.echoes.utils.UserPrefManager

@Composable
fun MainScreen(
    context: Context,
    viewModel: EchoesViewModel,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {}
) {
    val navController = rememberNavController()
    val currentScreen = remember { mutableStateOf<Screen>(Screen.Upload) }

    // Fetch user profile and news list
    LaunchedEffect(Unit) {
        //viewModel.fetchNewsList("id")
        viewModel.fetchProfileData { profile ->
            profile.id?.let { id ->
                viewModel.userId.value = id
                viewModel.setRewardPoints(profile.rewardPoint?.toInt())
                UserPrefManager.setUserId(context, id)
                viewModel.fetchNewsList(id)
                viewModel.fetchVouchersList()
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        modifier = Modifier.fillMaxWidth(),
        bottomBar = {
            BottomBar(
                currentScreen = currentScreen.value,
                onItemSelected = { screen ->
                    currentScreen.value = screen
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
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
                UploadedArticlesScreen(context = context, viewModel = viewModel, onArticleClick = {})
            }
            composable(Screen.Upload.route) {
                UploadArticle(viewModel = viewModel, context = context)
            }
            composable(Screen.Profile.route) {
                ProfileSection(viewModel = viewModel, context = context, onLogout = onLogout)
            }
            composable(Screen.Rewards.route) {
                RewardsScreen(viewModel = viewModel, context = context)
            }
        }
    }
}


