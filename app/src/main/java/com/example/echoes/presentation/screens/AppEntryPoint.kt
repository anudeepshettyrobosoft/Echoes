package com.example.echoes.presentation.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.echoes.presentation.viewmodel.EchoesViewModel
import com.example.echoes.utils.UserPrefManager

@Composable
fun AppEntryPoint(
    context: Context,
    viewModel: EchoesViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    // State for login and registration
    val isLoggedIn = remember { mutableStateOf(UserPrefManager.isUserLoggedIn(context)) }
    val isRegistered = remember { mutableStateOf(UserPrefManager.isUserRegistered(context)) }

    NavHost(
        navController = navController,
        startDestination = "splash"/*when {
            isLoggedIn.value -> "main"
            isRegistered.value -> "login"
            else -> "register"
        }*/
    ) {
        // Splash Screen
        composable("splash") {
            EchoesSplashScreen(
                onAnimationEnd = {
                    val destination = when {
                        isLoggedIn.value -> "main"
                        isRegistered.value -> "login"
                        else -> "register"
                    }
                    navController.navigate(destination) {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("main") {
            MainScreen(
                context = context,
                viewModel = viewModel,
                onLogout = {
                    viewModel.logoutUser {
                        if (it) {
                            isLoggedIn.value = false
                            UserPrefManager.clearUserData(context)
                            navController.navigate("login") {
                                popUpTo("main") { inclusive = true }
                            }
                        }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                viewModel = viewModel,
                onLoginClick = { email, password ->
                    viewModel.loginUser(context, email, password) { success ->
                        if (success) {
                            isLoggedIn.value = true // Update state
                            UserPrefManager.setUserLoggedIn(context, true)
                            navController.navigate("main") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegistrationScreen(
                viewModel = viewModel,
                onRegisterClick = { name, email, phone ->
                    viewModel.registerUser(name, email, phone) { success ->
                        if (success) {
                            UserPrefManager.saveUserData(context, name, email, phone)
                            UserPrefManager.setUserRegistered(context, true)
                            isRegistered.value = true // Update state
                            navController.navigate("login") {
                                popUpTo("register") { inclusive = true }
                            }
                        }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }
    }
}


