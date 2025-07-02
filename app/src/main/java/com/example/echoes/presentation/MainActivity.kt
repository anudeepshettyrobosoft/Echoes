package com.example.echoes.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.echoes.presentation.screens.AppEntryPoint
import com.example.echoes.presentation.screens.MainScreen
import com.example.echoes.presentation.theme.EchoesTheme
import com.example.echoes.presentation.utils.SnackbarManager
import com.example.echoes.presentation.viewmodel.EchoesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: EchoesViewModel by viewModels()
            EchoesTheme {
                Scaffold(
                    snackbarHost = { SnackbarManager.AppSnackbarHost() },
                    containerColor = Color.White,
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    AppEntryPoint(
                        context = LocalContext.current,
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }

            }
        }
    }

}

























