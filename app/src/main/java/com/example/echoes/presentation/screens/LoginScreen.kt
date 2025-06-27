package com.example.echoes.presentation.screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echoes.R
import com.example.echoes.presentation.components.CustomOutlinedTextField
import com.example.echoes.presentation.components.defaultTextStyle
import com.example.echoes.utils.UserPrefManager.saveUserData
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

@Composable
fun RegistrationScreen(
    onRegister: (name: String, email: String, phone: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    val isFormValid = remember(name, email, phone) {
        name.isNotBlank() && email.isNotBlank() && phone.isNotBlank()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF7C4DFF),
                        Color(0xFFE8EAF6)
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Register",
                style = defaultTextStyle.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            CustomOutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = "Name",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Name Icon",
                        tint = Color.Gray
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            CustomOutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email Icon",
                        tint = Color.Gray
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            CustomOutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Phone",
                keyboardType = KeyboardType.Phone,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone Icon",
                        tint = Color.Gray
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onRegister(name, email, phone) },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when (isFormValid) {
                        true -> colorResource(id = R.color.code_7C4DFF)
                        else -> colorResource(id = R.color.code_B69BFF)
                    }
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Register", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf<String?>(null) }

    var isOtpSent by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isOtpSent) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phone)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(context as Activity)
                    .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                            auth.signInWithCredential(credential).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    saveUserData(context, name, email, phone)
                                    onLoginSuccess()
                                }
                            }
                        }

                        override fun onVerificationFailed(e: FirebaseException) {
                            Toast.makeText(
                                context,
                                "Verification failed: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onCodeSent(
                            id: String,
                            token: PhoneAuthProvider.ForceResendingToken
                        ) {
                            verificationId = id
                            isOtpSent = true
                        }
                    }).build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            }) {
                Text("Send OTP")
            }
        } else {
            TextField(
                value = otp,
                onValueChange = { otp = it },
                label = { Text("Enter OTP") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                val credential = PhoneAuthProvider.getCredential(verificationId ?: "", otp)
                auth.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        saveUserData(context, name, email, phone)
                        onLoginSuccess()
                    }
                }
            }) {
                Text("Verify OTP")
            }
        }
    }
}