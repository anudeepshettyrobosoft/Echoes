package com.example.echoes.presentation.screens

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.example.echoes.R
import com.example.echoes.domain.model.NewsItem
import com.example.echoes.domain.model.NewsStatus
import com.example.echoes.presentation.components.defaultTextStyle
import com.example.echoes.presentation.utils.state.ImageLoadingState
import com.example.echoes.presentation.viewmodel.EchoesViewModel
import com.example.echoes.utils.ImageUtils.getImageRequestWithHeaders


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