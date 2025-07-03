package com.example.echoes.presentation.screens

import android.content.Context
import android.view.ViewGroup
import android.webkit.WebView
import androidx.annotation.RawRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.echoes.R
import com.example.echoes.domain.model.BadgeItem
import com.example.echoes.domain.model.NewsStatus
import com.example.echoes.domain.model.ProfileData
import com.example.echoes.presentation.components.defaultTextStyle
import com.example.echoes.presentation.viewmodel.EchoesViewModel
import com.example.echoes.utils.ImageUtils.getImageRequestWithHeaders

@Composable
fun ProfileSection(
    viewModel: EchoesViewModel,
    context: Context,
    onLogout: () -> Unit = { /* Default no-op */ }
) {
    val profileData by viewModel.profileState.collectAsState()
    val newsData by viewModel.newsListState.collectAsState()
    var showHtmlDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()), // Constrain LazyColumn within Scaffold
            verticalArrangement = Arrangement.spacedBy(16.dp),

            ) {
            ProfileHeaderSection(context, profileData,onLogout)

            RewardPointsBanner(profileData?.rewardPoint ?: "0")


            ProfileSummaryCard(
                badgesCount = profileData?.badges?.size ?: 0,
                uploadedArticlesCount = newsData.size,
                publishedArticlesCount = newsData.filter {
                    it.status.contains(
                        NewsStatus.PUBLISHED.statusName,
                        ignoreCase = true
                    )
                }.size
            )


            BadgeSection(profileData?.badges)


            LearnAboutRewards { showHtmlDialog = true }

        }

        if (showHtmlDialog) {
            HtmlDialog(
                htmlUrl = "https://inspiring-lokum-f7faed.netlify.app/rewards.html",
                onDismiss = { showHtmlDialog = false }
            )
        }
    }
}

@Composable
fun ProfileSummaryCard(
    badgesCount: Int = 0,
    uploadedArticlesCount: Int = 0,
    publishedArticlesCount: Int = 0
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Your Summary",
                style = defaultTextStyle.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProfileSummaryTile(
                    icon = Icons.Default.Star,
                    iconColor = Color(0xFFF4C430),
                    label = "Badges",
                    count = badgesCount
                )
                ProfileSummaryTile(
                    icon = Icons.Default.List,
                    iconColor = Color(0xFF4A90E2),
                    label = "Uploaded",
                    count = uploadedArticlesCount
                )
                ProfileSummaryTile(
                    icon = Icons.Default.CheckCircle,
                    iconColor = Color(0xFF27AE60),
                    label = "Published",
                    count = publishedArticlesCount
                )
            }
        }
    }
}

@Composable
fun ProfileSummaryTile(
    icon: ImageVector,
    iconColor: Color,
    label: String,
    count: Int
) {
    Column(
        modifier = Modifier.width(90.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(iconColor.copy(alpha = 0.15f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = count.toString(),
            style = defaultTextStyle.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        )
        Text(
            text = label,
            style = defaultTextStyle.copy(
                fontSize = 12.sp,
                color = Color.Gray
            )
        )
    }
}




/*@Composable
fun ProfileSummaryCard(
    badgesCount: Int = 0,
    uploadedArticlesCount: Int = 0,
    publishedArticlesCount: Int = 0
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RectangleShape,
        elevation = CardDefaults.elevatedCardElevation(),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.code_F6F6F6))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileSummaryItem(
                count = badgesCount,
                label = "Badges",
                icon = Icons.Default.Star
            )

            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(colorResource(id = R.color.code_E8E8E8))
            )

            ProfileSummaryItem(
                count = uploadedArticlesCount,
                label = "Uploaded Articles",
                icon = Icons.Default.List
            )

            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(colorResource(id = R.color.code_E8E8E8))
            )

            ProfileSummaryItem(
                count = publishedArticlesCount,
                label = "Published Articles",
                icon = Icons.Default.CheckCircle
            )
        }
    }
}*/

@Composable
fun ProfileSummaryItem(
    count: Int,
    label: String,
    icon: ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            // tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = count.toString(),
            style = defaultTextStyle.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        )
        Text(
            text = label,
            style = defaultTextStyle.copy(
                fontSize = 12.sp,
            )
        )
    }
}

@Composable
fun BadgeSection(badgeItems: List<BadgeItem>? = emptyList()) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Your Badges",
            style = defaultTextStyle.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        if (badgeItems?.isNotEmpty() == true) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(badgeItems ?: listOf()) { badge ->
                    FullWidthBadgeCard(badge)
                }
            }
            /*Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                badgeItems.forEach { badge ->
                    FullWidthBadgeCard(badge)
                }
            }*/
        } else {
            EmptyBadgePlaceholder()
        }
    }
}

@Composable
fun FullWidthBadgeCard(badge: BadgeItem) {
    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .size(100.dp)
                .padding(4.dp),
            shape = CircleShape,
            elevation = CardDefaults.elevatedCardElevation(),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.code_F6F6F6))
        ) {
            badge.getBadgeIcon()?.let { badgeIcon ->
                Icon(
                    painter = painterResource(id = badgeIcon),
                    contentDescription = "${badge.badgeType} Badge",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    tint = Color.Unspecified
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = badge.badgeType,
            style = defaultTextStyle.copy(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
            textAlign = TextAlign.Center
        )
        Text(
            text = "x ${badge.count}",
            style = defaultTextStyle,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun EmptyBadgePlaceholder() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.code_F6F6F6))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 👇 Add a Lottie animation if available
            LottieAnimationView(
                resId = R.raw.no_badges, // replace with your actual file
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "No badges earned yet!",
                style = defaultTextStyle.copy(fontSize = 16.sp, fontWeight = FontWeight.Medium)
            )
            Text(
                text = "Upload articles, earn rewards, and get your first badge!",
                style = defaultTextStyle.copy(fontSize = 12.sp, color = Color.Gray),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun LottieAnimationView(
    @RawRes resId: Int,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier
    )
}



/*@Composable
fun EmptyBadgePlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(colorResource(id = R.color.code_F6F6F6)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "You have no badges yet!",
            style = defaultTextStyle
        )
    }
}*/


@Composable
fun LearnAboutRewards(onShowHtmlDialog: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_gift),
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = Color(0xFFFFA500)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Earn Rewards!", fontWeight = FontWeight.Bold)
                Text("Get vouchers by earning badges and uploading articles.")
            }
            TextButton(onClick = { onShowHtmlDialog() }) {
                Text("Learn More")
            }
        }
    }

    /* Card(
         modifier = Modifier
             .fillMaxWidth()
             .padding(horizontal = 16.dp),
         colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.code_F6F6F6)),
         elevation = CardDefaults.elevatedCardElevation()
     ) {
         Column(
             modifier = Modifier.padding(16.dp),
             verticalArrangement = Arrangement.spacedBy(8.dp)
         ) {
             Text(
                 text = "Earn rewards by collecting badges!",
                 style = defaultTextStyle.copy(
                     fontSize = 14.sp,
                 ),
             )
             Text(
                 text = "Unlock exclusive perks by achieving milestones in your journey.",
                 style = defaultTextStyle
             )
             Button(
                 onClick = { onShowHtmlDialog() },
                 modifier = Modifier.align(Alignment.End),
                 colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
             ) {
                 Text(text = "Learn More", color = Color.White)
             }
         }
     }*/
}


@Composable
fun HtmlDialog(htmlUrl: String, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false,
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            /*.height(500.dp)*/, // Adjust as per your needs
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                // Close Icon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        modifier = Modifier
                            .clickable { onDismiss() }
                            .padding(8.dp)
                    )
                }

                // WebView
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            settings.javaScriptEnabled = true
                            loadUrl(htmlUrl)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun ProfileHeaderSection(
    context: Context,
    profileData: ProfileData?,
    onLogout: () -> Unit
) {
    val profile = profileData ?: ProfileData()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF4A90E2), Color(0xFF6FB1FC))
                )
            )
            .padding(bottom = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = getImageRequestWithHeaders(context, profile.getImageUrl()),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color.White, CircleShape)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(profile.name, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Joined on ${profile.doj}", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
        }

        // 🔴 Logout Icon
        Icon(
            imageVector = Icons.Default.ExitToApp,
            contentDescription = "Logout",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .clickable { onLogout() },
            tint = Color.White
        )
    }
}

@Composable
fun RewardPointsBanner(points: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F0FF))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_gift), // your gift icon
                contentDescription = null,
                tint = Color(0xFF4A90E2),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Your Reward Points", fontWeight = FontWeight.Medium)
                Text("$points pts", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}




/*
@Composable
fun ProfileHeaderSection(context: Context, profileData: ProfileData?,onLogout: () -> Unit) {
    var profile = profileData
    if (profile == null) {
        profile = ProfileData()
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Logout icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Logout",
                modifier = Modifier
                    .clickable { onLogout() }
                    .padding(8.dp)
            )
        }
        AsyncImage(
            model = getImageRequestWithHeaders(context, profile.getImageUrl()),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, colorResource(id = R.color.code_C8DDF6), CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = profile.name, style = defaultTextStyle.copy(fontSize = 20.sp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Joined on ${profile.doj}",
            style = defaultTextStyle,
            color = colorResource(id = R.color.code_9)
        )
    }
}*/
