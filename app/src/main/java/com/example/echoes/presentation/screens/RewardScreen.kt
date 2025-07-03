package com.example.echoes.presentation.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.echoes.R
import com.example.echoes.presentation.components.defaultTextStyle
import com.example.echoes.presentation.viewmodel.EchoesViewModel

@Composable
fun RewardsScreen(
    context: Context,
    viewModel: EchoesViewModel
) {
    val vouchers = viewModel.vouchers.collectAsState().value

    val userPoints = viewModel.rewardPoints.collectAsState().value
    var redeemedCode by remember { mutableStateOf<String?>(null) }

    val redeemedVouchers = viewModel.redeemedVouchers.collectAsState().value

    Column(modifier = Modifier.fillMaxSize()) {

        // 🔹 Fancy Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF4A90E2), Color(0xFF70B2EE)),
                        startY = 0f,
                        endY = 600f
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Your Reward Points",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "$userPoints pts",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 Voucher List
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            items(vouchers) { voucher ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 18.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Optional icon/image
                        Image(
                            painter = painterResource(id = voucher.getVendorIcon()), // or any asset
                            contentDescription = null,
                            // tint = colorResource(id = R.color.code_666666),
                            modifier = Modifier
                                .size(60.dp)
                                .padding(end = 12.dp)
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = voucher.title,
                                style = defaultTextStyle.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Points Required: ${voucher.pointsRequired}",
                                style = defaultTextStyle.copy(
                                    color = colorResource(id = R.color.code_666666)
                                )
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.redeemVoucher(voucher.id){
                                    if(it){
                                        redeemedCode = voucher.code
                                    }
                                }
                                /*val updatedPoints = userPoints - voucher.pointsRequired
                                viewModel.setRewardPoints(updatedPoints)
                                redeemedCode = "ECHOES-${voucher.id}-XYZ"
                                viewModel.markVoucherRedeemed(voucher.id)*/
                            },
                            enabled = userPoints >= voucher.pointsRequired && !redeemedVouchers.contains(
                                voucher.id
                            ),
                            //enabled = userPoints >= voucher.pointsRequired,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (userPoints >= voucher.pointsRequired)
                                    Color(0xFF4A90E2) else Color.LightGray
                            )
                        ) {
                            Text(
                                if (redeemedVouchers.contains(voucher.id)) "Redeemed" else "Redeem",
                                style = defaultTextStyle
                            )
                        }
                    }
                }
            }
        }

        // 🔹 Voucher Redeemed Popup
        redeemedCode?.let {
            RedeemedDialog(
                code = it
            ) {
                redeemedCode = null
            }
        }
    }
}

@Composable
fun RedeemedDialog(
    code: String,
    onDismiss: () -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.confetti))
    val clipboardManager = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000)) // semi-transparent black
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(320.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(Color(0xFFFDEBD0), Color(0xFFFFFFFF))
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                // 🎉 Fullscreen Lottie Confetti
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.fillMaxSize()
                )

                // 🧾 Voucher Code & Copy Button
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🎁 You've Redeemed!",
                        style = defaultTextStyle.copy(
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.code_666666),
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        color = Color.White.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(12.dp),
                        shadowElevation = 6.dp,
                        tonalElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = code,
                                style = defaultTextStyle.copy(
                                    color = Color.Black,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = {
                                clipboardManager.setText(AnnotatedString(code))
                                copied = true
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_copy),
                                    contentDescription = "Copy",
                                    tint = colorResource(id = R.color.code_666666),
                                )
                            }
                        }
                    }

                    if (copied) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Copied to clipboard!", color = Color.DarkGray, fontSize = 12.sp)
                    }
                }

                // ❌ Close Icon
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}