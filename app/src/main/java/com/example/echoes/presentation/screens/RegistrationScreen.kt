package com.example.echoes.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.echoes.R
import com.example.echoes.presentation.components.CustomOutlinedTextField
import com.example.echoes.presentation.components.defaultTextStyle
import com.example.echoes.presentation.viewmodel.EchoesViewModel
import com.example.echoes.utils.LoadingIndicator

@Composable
fun RegistrationScreen(
    viewModel: EchoesViewModel,
    onRegisterClick: (String, String, String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val isLoading by viewModel.isLoading

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var nameInteracted by remember { mutableStateOf(false) }
    var emailInteracted by remember { mutableStateOf(false) }
    var phoneInteracted by remember { mutableStateOf(false) }

    val isButtonEnabled by remember {
        derivedStateOf {
            name.isNotBlank() &&
                    isValidEmail(email) &&
                    isValidPhone(phone)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Top Gradient Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF4A90E2), Color.Transparent),
                        startY = 0f,
                        endY = 500f
                    )
                )
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = R.mipmap.ic_launcher), // Replace with your logo
                    contentDescription = "App Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Registration Text
            Text(
                text = "Register",
                style = defaultTextStyle.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    color = colorResource(id = R.color.code_666666)
                ),
                modifier = Modifier.padding(bottom = 24.dp),
               // color = Color.Black
            )

            // Name Input
            CustomOutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameInteracted = true // Mark as interacted when name changes
                },
                label = "Name",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = nameInteracted && name.isEmpty(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email Input
            CustomOutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailInteracted = true // Mark as interacted when email changes
                },
                label = "Email",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = emailInteracted && !isValidEmail(email),
                keyboardType = KeyboardType.Email,
            )
            if (!isValidEmail(email) && email.isNotEmpty()) {
                Text(
                    text = "Invalid email address",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Input
            CustomOutlinedTextField(
                value = phone,
                onValueChange = {
                    phone = it
                    phoneInteracted = true // Mark as interacted when phone changes
                },
                label = "Phone",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = phoneInteracted && !isValidPhone(phone),
                keyboardType = KeyboardType.Phone,
            )
            if (!isValidPhone(phone) && phone.isNotEmpty()) {
                Text(
                    text = "Invalid phone number",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Register Button
            Button(
                onClick = { onRegisterClick(name, email, phone) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when (isButtonEnabled) {
                        true -> colorResource(id = R.color.code_4a90E2)
                        else -> colorResource(id = R.color.code_C8DDF6)
                    }
                ),
            ) {
                Text("Register")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigate to Login Button
            TextButton(
                onClick = { onNavigateToLogin() },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = colorResource(id = R.color.code_666666)
                )
            ) {
                Text(text = "Already have an account? Login here.", style = defaultTextStyle)
            }
        }

        LoadingIndicator(isLoading = isLoading)
    }
}


fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidPhone(phone: String): Boolean {
    return phone.length == 10 && phone.all { it.isDigit() }
}


