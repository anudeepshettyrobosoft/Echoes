package com.example.echoes.presentation.screens

import android.content.Context
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
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
    context: Context
) {
    val profileData by viewModel.profileState.collectAsState()
    val newsData by viewModel.newsListState.collectAsState()
    var showHtmlDialog by remember { mutableStateOf(false) }
    listOf("Published an article", "Earned a badge", "Commented on an article")

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
            ProfileHeaderSection(context, profileData)


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
}

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                badgeItems.forEach { badge ->
                    FullWidthBadgeCard(badge)
                }
            }
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
}


@Composable
fun LearnAboutRewards(onShowHtmlDialog: () -> Unit) {
    Card(
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
    }
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
fun ProfileHeaderSection(context: Context, profileData: ProfileData?) {
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
        AsyncImage(
            model = getImageRequestWithHeaders(context, profile.getImageUrl()),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, colorResource(id = R.color.code_B4B4B4), CircleShape),
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
}