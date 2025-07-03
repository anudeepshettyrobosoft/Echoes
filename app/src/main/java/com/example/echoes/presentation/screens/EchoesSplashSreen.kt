package com.example.echoes.presentation.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echoes.R
import kotlinx.coroutines.delay

/*@Composable
fun EchoesSplashScreen1(
    modifier: Modifier = Modifier,
    onAnimationEnd: () -> Unit = {}
) {
    val transition = rememberInfiniteTransition()

    // Animate stroke drawing from 0f to 1f
    val arcProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(2000, easing = FastOutSlowInEasing),
            RepeatMode.Restart
        )
    )

    val textAlpha by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(1000, delayMillis = 2500, easing = LinearEasing),
            RepeatMode.Restart
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF4A90E2), Color(0xFF70B2EE))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(250.dp)) { // Slightly bigger canvas
            val center = size / 2f
            val stroke = Stroke(width = 6f, cap = StrokeCap.Round)

            // ✅ Center Arc (unchanged)
            drawArc(
                color = Color.White,
                startAngle = 135f,
                sweepAngle = 180f * arcProgress,
                useCenter = false,
                style = stroke,
                size = Size(50f, 50f),
                topLeft = Offset(center.width - 25f, center.height - 25f)
            )

            // ✅ RIGHT side arc (was left earlier, now moved to right)
            drawArc(
                color = Color.White,
                startAngle = 220f,
                sweepAngle = 180f * arcProgress,
                useCenter = false,
                style = stroke,
                size = Size(130f, 130f), // Increased size for spacing
                topLeft = Offset(center.width - 65f, center.height - 65f)
            )

            // ✅ LEFT side two arcs (were on right earlier, now swapped)
            drawArc(
                color = Color.White,
                startAngle = 90f,
                sweepAngle = 180f * arcProgress,
                useCenter = false,
                style = stroke,
                size = Size(160f, 160f), // First left arc (bigger spacing)
                topLeft = Offset(center.width - 80f, center.height - 80f)
            )

            drawArc(
                color = Color.White,
                startAngle = 90f,
                sweepAngle = 180f * arcProgress,
                useCenter = false,
                style = stroke,
                size = Size(195f, 195f), // Second left arc (outer)
                topLeft = Offset(center.width - 97.5f, center.height - 97.5f)
            )
        }

        // Echoes text appearing with ripple effect
        Text(
            text = "Echoes",
            color = Color.White.copy(alpha = textAlpha),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 200.dp)
                .graphicsLayer {
                    scaleX = textAlpha
                    scaleY = textAlpha
                }
        )
    }

    // Automatically trigger end after 4s
    LaunchedEffect(Unit) {
        delay(4000)
        onAnimationEnd()
    }
}

@Composable
fun EchoesSplashScreen(
    modifier: Modifier = Modifier,
    onAnimationEnd: () -> Unit = {}
) {
    val transition = rememberInfiniteTransition()

    // Animate stroke drawing from 0f to 1f
    val arcProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(2000, easing = FastOutSlowInEasing),
            RepeatMode.Restart
        )
    )

    val textAlpha by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(1000, delayMillis = 2500, easing = LinearEasing),
            RepeatMode.Restart
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF4A90E2), Color(0xFF70B2EE))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(250.dp)) { // Slightly bigger canvas
            val center = size / 2f
            val stroke = Stroke(width = 6f, cap = StrokeCap.Round)

            // ✅ Center Arc (unchanged)
            drawArc(
                color = Color.White,
                startAngle = 135f,
                sweepAngle = 180f * arcProgress,
                useCenter = false,
                style = stroke,
                size = Size(50f, 50f),
                topLeft = Offset(center.width - 25f, center.height - 25f)
            )

            // ✅ RIGHT side arc (was left earlier, now moved to right)
            drawArc(
                color = Color.White,
                startAngle = 220f,
                sweepAngle = 180f * arcProgress,
                useCenter = false,
                style = stroke,
                size = Size(130f, 130f), // Increased size for spacing
                topLeft = Offset(center.width - 65f, center.height - 65f)
            )

            // ✅ LEFT side two arcs (were on right earlier, now swapped)
            drawArc(
                color = Color.White,
                startAngle = 90f,
                sweepAngle = 180f * arcProgress,
                useCenter = false,
                style = stroke,
                size = Size(160f, 160f), // First left arc (bigger spacing)
                topLeft = Offset(center.width - 80f, center.height - 80f)
            )

            drawArc(
                color = Color.White,
                startAngle = 90f,
                sweepAngle = 180f * arcProgress,
                useCenter = false,
                style = stroke,
                size = Size(195f, 195f), // Second left arc (outer)
                topLeft = Offset(center.width - 97.5f, center.height - 97.5f)
            )
        }

        // Echoes text appearing with ripple effect
        Text(
            text = "Echoes",
            color = Color.White.copy(alpha = textAlpha),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 200.dp)
                .graphicsLayer {
                    scaleX = textAlpha
                    scaleY = textAlpha
                }
        )
    }

    // Automatically trigger end after 4s
    LaunchedEffect(Unit) {
        delay(4000)
        onAnimationEnd()
    }
}*/

@Composable
fun EchoesSplashScreen(
    modifier: Modifier = Modifier,
    onAnimationEnd: () -> Unit = {}
) {
    val logoBlue = Color(0xFF4A90E2) // Replace with exact color from your logo

    var showText by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(logoBlue),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Arcs Image
            Image(
                painter = painterResource(id = R.drawable.arcs), // Your extracted arcs image
                contentDescription = "Echoes Arcs",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Animated Echoes Text
            if (showText) {
                AnimatedEchoesText()
            }
        }
    }

    // Trigger text animation after delay
    LaunchedEffect(Unit) {
        delay(1000)
        showText = true
        delay(3000)
        onAnimationEnd()
    }
}

@Composable
fun AnimatedEchoesText() {
    val text = "Echoes"
    Row {
        text.forEachIndexed { index, char ->
            val animation = remember { Animatable(0f) }
            LaunchedEffect(key1 = index) {
                delay(index * 120L) // Slight stagger between characters
                animation.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(500, easing = FastOutSlowInEasing)
                )
            }

            Text(
                text = char.toString(),
                color = Color.White.copy(alpha = animation.value),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .graphicsLayer {
                        translationY = (1f - animation.value) * 20f
                    }
            )
        }
    }
}
















