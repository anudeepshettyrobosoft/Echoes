package com.example.echoes.presentation.screens

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.echoes.R
import com.example.echoes.data.model.UploadNewsRequest
import com.example.echoes.presentation.components.CustomOutlinedTextField
import com.example.echoes.presentation.components.CustomPopup
import com.example.echoes.presentation.components.DropDownView
import com.example.echoes.presentation.components.FieldLabel
import com.example.echoes.presentation.components.defaultTextStyle
import com.example.echoes.presentation.viewmodel.EchoesViewModel
import com.example.echoes.utils.AppManager
import com.example.echoes.utils.DateTimePickerUtils
import com.example.echoes.utils.DateTimePickerUtils.showTimePickerDialog
import com.example.echoes.utils.ImageUtils.saveBitmapToLocalUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun UploadArticle(
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

    val isButtonEnabled by remember {
        derivedStateOf {
            title.isNotBlank() &&
                    description.isNotBlank()
        }
    }

    var mediaUri by remember { mutableStateOf<Uri?>(null) }

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val storagePermissionState =
        rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)

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
            options = AppManager.Categories,
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
                                DateTimePickerUtils.showDatePicker(
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

