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
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF4A90E2), Color(0xFF70B2EE))
                        )
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "Your Articles",
                    style = defaultTextStyle.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (uploadedNews.isEmpty().not())
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text("Search articles...", color = Color.White)
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                tint = Color.White,
                                contentDescription = null
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White
                        )
                    )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(Color.White)
                .fillMaxSize()
        ) {

            if (uploadedNews.isEmpty().not())
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filters = listOf("All") + NewsStatus.entries.map { it.statusName }
                    filters.forEach { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = {
                                Text(
                                    filter, style = defaultTextStyle.copy(
                                        color = if (selectedFilter == filter) Color.White else colorResource(
                                            id = R.color.code_1E1E1E
                                        )
                                    )
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = colorResource(id = R.color.code_4a90E2),
                                disabledLabelColor = colorResource(id = R.color.code_4a90E2),
                            ),
                            leadingIcon = if (selectedFilter == filter) {
                                {
                                    Icon(
                                        Icons.Default.Check,
                                        tint = Color.White,
                                        contentDescription = null
                                    )
                                }
                            } else null
                        )
                    }
                }

            if (filteredNews.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieAnimationView(
                        resId = R.raw.no_articles,
                        modifier = Modifier.size(200.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No articles found", style = defaultTextStyle.copy(fontSize = 18.sp))
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredNews) { newsItem ->
                        NewsCard(context, newsItem)
                    }
                }
            }
        }
    }
}

@Composable
fun NewsCard(context: Context, newsItem: NewsItem) {
    val statusColor = when (newsItem.status.lowercase()) {
        "published" -> Color(0xFF4CAF50)
        "rejected" -> Color(0xFFF44336)
        else -> Color(0xFFFFC107)
    }


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
        Row(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = getImageRequestWithHeaders(context, newsItem.imageORVideoUrl ?: ""),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    newsItem.title,
                    style = defaultTextStyle.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    ),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    newsItem.description,
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
                    Text(newsItem.status, style = defaultTextStyle.copy(color = statusColor))

                    newsItem.submittedDate?.let {
                        Text(
                            text = it,
                            style = defaultTextStyle.copy(
                                fontSize = 10.sp,
                                color = colorResource(id = R.color.code_1E1E1E)
                            )
                        )
                    }

                }
                if (newsItem.status.equals(NewsStatus.REJECTED.statusName, ignoreCase = true) &&
                    newsItem.comments.isNullOrEmpty().not()
                ) {
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
                }
            }
        }
    }
}
