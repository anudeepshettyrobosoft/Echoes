package com.example.echoes

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.echoes.network.model.BadgeItem
import com.example.echoes.network.model.NewsItem
import com.example.echoes.network.model.NewsStatus
import com.example.echoes.network.model.ProfileData
import com.example.echoes.network.model.UploadNewsRequest
import com.example.echoes.ui.compose.FancyBottomBar
import com.example.echoes.ui.theme.CustomOutlinedTextField
import com.example.echoes.ui.theme.CustomPopup
import com.example.echoes.ui.theme.DropDownView
import com.example.echoes.ui.theme.EchoesTheme
import com.example.echoes.ui.theme.FieldLabel
import com.example.echoes.ui.theme.defaultTextStyle
import com.example.echoes.util.ImageLoadingState
import com.example.echoes.util.Screen
import com.example.echoes.util.SnackbarManager
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit


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
                    MainScreen(
                        context = LocalContext.current,
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }

            }
        }
    }

}

@Composable
fun MainScreen(
    context: Context,
    viewModel: EchoesViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val currentScreen = remember { mutableStateOf(Screen.Upload.route) }

    val uiState by viewModel.uploadState.collectAsState()

    val isLoggedIn = remember { mutableStateOf(true) }

    if (isLoggedIn.value) {
        // Main App Navigation
        Scaffold(
            containerColor = Color.White,
            modifier = Modifier.fillMaxWidth(),
            bottomBar = {
                FancyBottomBar(
                    currentScreen = remember { mutableStateOf(Screen.Upload.route) }.value,
                    onItemSelected = { screen ->
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
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
                    UploadedArticlesScreen(
                        context = context,
                        viewModel = viewModel,
                        onArticleClick = {}
                    )
                }
                composable(Screen.Upload.route) {
                    UploadNewsUI(viewModel = viewModel, context = context)
                }
                composable(Screen.Profile.route) {
                    ProfileSection(viewModel = viewModel, context = context)
                }
            }
        }
    } else {
       RegistrationScreen{name,email,phone->

       }
        /*// Show Login Screen
        LoginScreen(onLoginSuccess = {
            isLoggedIn.value = true
        })*/
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun UploadNewsUI(
    context: Context,
    modifier: Modifier = Modifier,
    viewModel: EchoesViewModel
) {
    val shouldShowSuccessPopup by viewModel.shouldShowSuccessPopup.collectAsState()
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Select category") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    val selectedImages by viewModel.selectedImages.collectAsState()

    val categories = listOf("General", "Politics", "Sports", "Entertainment", "Local")

    val isButtonEnabled by remember {
        derivedStateOf {
            title.isNotBlank() &&
                    description.isNotBlank()
        }
    }

    var mediaUri by remember { mutableStateOf<Uri?>(null) }

    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val storagePermissionState =
        rememberPermissionState(android.Manifest.permission.READ_EXTERNAL_STORAGE)

    val videoFile = remember {
        File(
            context.getExternalFilesDir(Environment.DIRECTORY_MOVIES),
            "video_${System.currentTimeMillis()}.mp4"
        )
    }
    val videoUri = remember {
        FileProvider.getUriForFile(context, "${context.packageName}.provider", videoFile)
    }


    val launcherVideo =
        rememberLauncherForActivityResult(ActivityResultContracts.CaptureVideo()) { isSuccess ->
            if (isSuccess) {
                mediaUri = videoUri // Set the captured video's URI
            }
        }

    val launcherCamera =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                mediaUri = saveBitmapToLocalUri(
                    bitmap,
                    context
                ) // Save bitmap to local file and get its URI
            }
        }

    val launcherGallery =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            //mediaUri = uri
            //selectedImages.addAll(uris)
            viewModel.addImage(uris)
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Upload Article",
            style = defaultTextStyle.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Start
        )

        FieldLabel(
            label = "Headline",
            isMandatory = true
        )

        CustomOutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = "Enter news headline",
            keyboardType = KeyboardType.Text
        )

        Spacer(modifier = Modifier.height(14.dp))

        FieldLabel(
            label = "Category",
            isMandatory = true
        )

        DropDownView(
            options = categories,
            selectedOption = category,
            onOptionSelected = {
                category = it
            },
            optionLabel = { it -> it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))


        FieldLabel(
            label = "Description",
            isMandatory = true
        )

        CustomOutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = "News Description",
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = {
                    if (!storagePermissionState.status.isGranted) {
                        storagePermissionState.launchPermissionRequest()
                    } else {
                        launcherGallery.launch("image/*")
                    }
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, colorResource(id = R.color.code_E8E8E8)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colorResource(id = R.color.code_1E1E1E),
                    containerColor = colorResource(id = R.color.code_F6F6F6)
                )
            ) {
                Text("Upload file")
                Spacer(modifier = Modifier.width(4.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_backup),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedButton(
                onClick = {
                    if (!cameraPermissionState.status.isGranted) {
                        cameraPermissionState.launchPermissionRequest()
                    } else {
                        launcherCamera.launch(null)
                    }
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, colorResource(id = R.color.code_E8E8E8)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colorResource(id = R.color.code_1E1E1E),
                    containerColor = colorResource(id = R.color.code_F6F6F6)
                )
            ) {

                Text("Capture")
                Spacer(modifier = Modifier.width(4.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_camera),
                    contentDescription = null
                )
            }
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(selectedImages) { imageUri ->
                Box(modifier = Modifier.size(80.dp)) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Gray)
                    )
                    IconButton(
                        onClick = { viewModel.removeImage(imageUri) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(18.dp)
                            .background(Color.White, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove",
                            tint = Color.Black
                        )
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(14.dp))

        FieldLabel(
            label = "Location"
        )
        CustomOutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = "Enter location",
            keyboardType = KeyboardType.Text,
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = "Location",
                    tint = colorResource(id = R.color.code_1E1E1E)
                )
            }
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row {
            CustomOutlinedTextField(
                modifier = Modifier.weight(1f),
                value = date,
                onValueChange = { date = it },
                label = "Date",
                keyboardType = KeyboardType.Text,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = "Location",
                        tint = colorResource(id = R.color.code_1E1E1E),
                        modifier = Modifier
                            .clickable {
                                showDatePicker(
                                    context = context,
                                    onDateSelected = { selectedDate ->
                                        date = selectedDate
                                    }
                                )
                            }
                    )
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            CustomOutlinedTextField(
                modifier = Modifier.weight(1f),
                value = time,
                onValueChange = { time = it },
                label = "Time",
                keyboardType = KeyboardType.Text,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_clock),
                        contentDescription = "Location",
                        tint = colorResource(id = R.color.code_1E1E1E),
                        modifier = Modifier
                            .clickable {
                                showTimePickerDialog(
                                    context = context,
                                    initialHour = Calendar.getInstance()
                                        .get(Calendar.HOUR_OF_DAY),
                                    initialMinute = Calendar.getInstance()
                                        .get(Calendar.MINUTE)
                                ) { hour, minute ->
                                    time =
                                        String.format(Locale.US, "%02d:%02d", hour, minute)
                                }
                            }
                    )
                }
            )
        }



        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(
            onClick = {
                if (isButtonEnabled) {
                    viewModel.uploadNews(
                        UploadNewsRequest(
                            title = title,
                            category = category,
                            description = description,
                            location = location,
                            reportedDate = date,
                            reportedTime = time
                        )
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = when (isButtonEnabled) {
                    true -> colorResource(id = R.color.code_7C4DFF)
                    else -> colorResource(id = R.color.code_B69BFF)
                }
            ),
        ) {
            Text("Submit", fontSize = 18.sp, color = Color.White)
        }
    }

    if (shouldShowSuccessPopup) {
        CustomPopup(message = "Your article was submitted successfully!!!", onDismiss = {
            viewModel.hideSuccessPopup()
            title = ""
        })
    }


}

@Composable
fun ProfileSection(
    viewModel: EchoesViewModel,
    context: Context
) {
    val profileData by viewModel.profileState.collectAsState()
    val newsData by viewModel.newsListState.collectAsState()
    var showHtmlDialog by remember { mutableStateOf(false) }
    val recentActivities =
        listOf("Published an article", "Earned a badge", "Commented on an article")

    // Scaffold setup
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
                badgesCount =profileData?.badges?.size ?: 0,
                uploadedArticlesCount = newsData.size,
                publishedArticlesCount = newsData.filter { it.status.contains(NewsStatus.PUBLISHED.statusName, ignoreCase = true) }.size
            )


            BadgeSectionFullWidth(profileData?.badges)


            LearnAboutRewards { showHtmlDialog = true }

        }

        // HTML Dialog
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
fun BadgeSectionFullWidth(badgeItems: List<BadgeItem>?= emptyList()) {
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


@Composable
fun NewsSummaryRow(context: Context, newsItem: NewsItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 10.dp),
        elevation = CardDefaults.elevatedCardElevation(),
        border = BorderStroke(
            width = 1.dp,
            color = colorResource(id = R.color.code_E8E8E8)
        ),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail Container
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorResource(id = R.color.code_E8E8E8)), // Placeholder background color
                contentAlignment = Alignment.Center
            ) {
                var imageState by remember { mutableStateOf(ImageLoadingState.LOADING) }

                AsyncImage(
                    model = getImageRequestWithHeaders(context, newsItem.imageORVideoUrl ?: ""),
                    contentDescription = "News Picture",
                    modifier = Modifier
                        .fillMaxSize()
                        .onGloballyPositioned { /* Track dimensions if needed */ },
                    contentScale = ContentScale.Crop,
                    onState = { state ->
                        imageState = when (state) {
                            is AsyncImagePainter.State.Success -> ImageLoadingState.SUCCESS
                            is AsyncImagePainter.State.Error -> ImageLoadingState.ERROR
                            else -> ImageLoadingState.LOADING
                        }
                    }
                )

                when (imageState) {
                    ImageLoadingState.LOADING -> {
                        CircularProgressIndicator(
                            color = colorResource(id = R.color.code_B4B4B4),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    ImageLoadingState.ERROR -> {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Failed to Load Image",
                            tint = colorResource(id = R.color.code_B4B4B4),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    ImageLoadingState.SUCCESS -> {
                        // No overlay for successful image
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Title
                Text(
                    text = newsItem.title,
                    style = defaultTextStyle.copy(
                        fontSize = 14.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = newsItem.description,
                    style = defaultTextStyle,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = newsItem.status,
                        style = defaultTextStyle.copy(fontWeight = FontWeight.SemiBold),
                        color = colorResource(id = R.color.code_1E1E1E)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    newsItem.submittedDate?.let {
                        Text(
                            text = "Submitted on: ${newsItem.submittedDate}",
                            style = defaultTextStyle.copy(
                                fontSize = 10.sp,
                                color = colorResource(id = R.color.code_B4B4B4)
                            )
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun RejectedNewsSummaryRow(context: Context, newsItem: NewsItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 10.dp),
        elevation = CardDefaults.elevatedCardElevation(),
        border = BorderStroke(
            width = 1.dp,
            color = colorResource(id = R.color.code_E8E8E8)
        ),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail Container
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorResource(id = R.color.code_E8E8E8)), // Placeholder background color
                contentAlignment = Alignment.Center
            ) {
                var imageState by remember { mutableStateOf(ImageLoadingState.LOADING) }

                AsyncImage(
                    model = newsItem.getImageUrl(),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    onState = { state ->
                        imageState = when (state) {
                            is AsyncImagePainter.State.Success -> ImageLoadingState.SUCCESS
                            is AsyncImagePainter.State.Error -> ImageLoadingState.ERROR
                            else -> ImageLoadingState.LOADING
                        }
                    }
                )

                when (imageState) {
                    ImageLoadingState.LOADING -> {
                        CircularProgressIndicator(
                            color = colorResource(id = R.color.code_B4B4B4),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    ImageLoadingState.ERROR -> {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Failed to Load Image",
                            tint = colorResource(id = R.color.code_B4B4B4),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    ImageLoadingState.SUCCESS -> {
                        // No overlay for successful image
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Title
                Text(
                    text = newsItem.title,
                    style = defaultTextStyle.copy(
                        fontSize = 14.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = newsItem.status,
                        style = defaultTextStyle.copy(fontWeight = FontWeight.SemiBold),
                        color = colorResource(id = R.color.code_1E1E1E)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Reason:",
                        style = defaultTextStyle.copy(color = colorResource(id = R.color.code_1E1E1E)),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = newsItem.comments ?: "No comments",
                        style = defaultTextStyle.copy(color = Color.Red),
                    )
                }

                newsItem.submittedDate?.let {
                    Text(
                        text = "Submitted on: ${newsItem.submittedDate}",
                        style = defaultTextStyle.copy(
                            fontSize = 10.sp,
                            color = colorResource(id = R.color.code_B4B4B4)
                        )
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadedArticlesScreen(
    context: Context,
    viewModel: EchoesViewModel,
    onArticleClick: (NewsItem) -> Unit
) {
    val uploadedNews by viewModel.newsListState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }

    val filteredNews = uploadedNews.filter {
        val formattedStatus = it.status.lowercase().replaceFirstChar { char -> char.uppercase() }
        (searchQuery.isEmpty() ||
                it.title.contains(searchQuery, ignoreCase = true) ||
                formattedStatus.contains(searchQuery, ignoreCase = true)) &&
                (selectedFilter == "All" || formattedStatus == selectedFilter)
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Articles",
                    style = defaultTextStyle.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = "Search articles...", style = defaultTextStyle.copy(
                                fontSize = 14.sp, color = colorResource(
                                    id = R.color.code_9
                                )
                            )
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(id = R.color.code_9),
                        unfocusedBorderColor = colorResource(id = R.color.code_9)
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            tint = colorResource(id = R.color.code_9),
                            contentDescription = null
                        )
                    }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(Color.White)
        ) {
            // Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filters = mutableListOf("All")
                filters.addAll(NewsStatus.entries.map { it.statusName })

                //  val filters = listOf("All", "Submitted", "Pending","Published", "Rejected")
                filters.forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter) },
                        leadingIcon = if (selectedFilter == filter) {
                            { Icon(Icons.Default.Check, contentDescription = null) }
                        } else null,
                    )
                }
            }

            // Grid View
            if (filteredNews.isEmpty()) {
                EmptyState(
                    onRetry = { /* Retry fetching news if needed */ }
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(filteredNews) { newsItem -> // Use filteredNews here
                        newsItem.status.let {
                            if (it.equals(NewsStatus.REJECTED.statusName, ignoreCase = true)) {
                                RejectedNewsSummaryRow(context, newsItem)
                            } else {
                                NewsSummaryRow(context, newsItem)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_backup),
            contentDescription = "No Articles",
            modifier = Modifier.size(120.dp),
            tint = colorResource(id = R.color.code_9)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No articles uploaded yet!",
            style = defaultTextStyle.copy(
                fontSize = 18.sp,
                color = colorResource(id = R.color.code_9)
            ),
        )
    }
}

fun saveBitmapToLocalUri(bitmap: Bitmap, context: Context): Uri? {
    val filename = "IMG_${System.currentTimeMillis()}.jpg"
    val file = File(context.cacheDir, filename)

    return try {
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

private fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            calendar.set(selectedYear, selectedMonth, selectedDay)
            val formattedDate =
                SimpleDateFormat("dd MMM yyyy", Locale.US).format(calendar.time)
            onDateSelected(formattedDate)
        },
        year, month, 1
    )
    datePickerDialog.show()
}

private fun showTimePickerDialog(
    context: Context,
    initialHour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
    initialMinute: Int = Calendar.getInstance().get(Calendar.MINUTE),
    is24HourView: Boolean = true,
    onTimeSelected: (hour: Int, minute: Int) -> Unit
) {
    TimePickerDialog(
        context,
        { _, hour, minute ->
            onTimeSelected(hour, minute)
        },
        initialHour,
        initialMinute,
        is24HourView
    ).show()
}

fun getImageRequestWithHeaders(context: Context, imageUrl: String): ImageRequest {
    return ImageRequest.Builder(context)
        .data(imageUrl)
        .addHeader("ngrok-skip-browser-warning", "true")
        .addHeader("User-Agent", "Custom-Agent")
        .diskCachePolicy(CachePolicy.ENABLED) // Optional: Enable disk caching
        .networkCachePolicy(CachePolicy.ENABLED) // Optional: Enable network caching
        .build()
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
                            Toast.makeText(context, "Verification failed: ${e.message}", Toast.LENGTH_LONG).show()
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

fun saveUserData(context: Context, name: String, email: String, phone: String) {
    val sharedPreferences = context.getSharedPreferences("EchoesApp", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putString("name", name)
        putString("email", email)
        putString("phone", phone)
        putBoolean("isLoggedIn", true)
        apply()
    }
}

fun checkLoginStatus(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("EchoesApp", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("isLoggedIn", false)
}

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















