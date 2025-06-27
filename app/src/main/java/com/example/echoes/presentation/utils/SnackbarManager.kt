package com.example.echoes.presentation.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object SnackbarManager {
    private val snackbarHostState = SnackbarHostState()

    @Composable
    fun AppSnackbarHost(
        modifier: Modifier = Modifier
            .fillMaxWidth()
           // .padding(horizontal = 16.dp, vertical = 8.dp)
            .wrapContentHeight(Alignment.Top)
    ) {
        SnackbarHost(hostState = snackbarHostState, modifier = modifier)
    }

    fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            snackbarHostState.showSnackbar(message, actionLabel, duration = duration)
        }
    }
}
