package com.example.echoes

import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echoes.network.NewsRepository
import com.example.echoes.network.model.NewsItem
import com.example.echoes.network.model.ProfileData
import com.example.echoes.network.model.UploadNewsRequest
import com.example.echoes.network.model.UploadNewsResponse
import com.example.echoes.usecases.GetNewsListUseCase
import com.example.echoes.usecases.GetProfileDataUseCase
import com.example.echoes.usecases.UploadNewsUseCase
import com.example.echoes.util.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class EchoesViewModel @Inject constructor(
    private val uploadNewsUseCase: UploadNewsUseCase,
    private val getNewsListUseCase: GetNewsListUseCase,
    private val getProfileDataUseCase: GetProfileDataUseCase,
    private val contentResolver: android.content.ContentResolver
) : ViewModel() {
    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> get() = _uploadState

    private val _newsListState = MutableStateFlow<List<NewsItem>>(emptyList())
    val newsListState: StateFlow<List<NewsItem>> get() = _newsListState

    private val _shouldShowSuccessPopup = MutableStateFlow<Boolean>(false)
    val shouldShowSuccessPopup: StateFlow<Boolean> get() = _shouldShowSuccessPopup

    private val _profileState = MutableStateFlow<ProfileData?>(null)
    val profileState: StateFlow<ProfileData?> get() = _profileState

    private val _selectedImages = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImages: StateFlow<List<Uri>> get() = _selectedImages

    init {
        fetchNewsList()
        fetchProfileData()
    }



    fun uploadNews(newsRequest: UploadNewsRequest) {
        Log.d(TAG, "${newsRequest.title}, ${newsRequest.category}, ${newsRequest.description}")
        viewModelScope.launch {
            _uploadState.value = UploadState.Loading
            val result = uploadNewsUseCase(newsRequest.copy(
                imageList = _selectedImages.value.ifEmpty { null }
            ))

            result.onSuccess { response ->
                Log.d(TAG, "Upload success")
                _shouldShowSuccessPopup.value = true
                fetchNewsList()
                _uploadState.value = UploadState.Success(response)
            }.onFailure { exception ->
                Log.e(TAG, "Upload failed with exception", exception)
                val errorMessage = exception.message ?: "Something went wrong"
                SnackbarManager.showSnackbar(errorMessage)
                _uploadState.value = UploadState.Error(errorMessage)
            }
        }
    }

    private fun fetchNewsList() {
        viewModelScope.launch {
            val result = getNewsListUseCase()
            if (result.isSuccess) {
                _newsListState.value = result.getOrThrow()
            } else {
                Log.e(TAG, "Error fetching news: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    private fun fetchProfileData() {
        viewModelScope.launch {
            val result = getProfileDataUseCase()
            if (result.isSuccess) {
                _profileState.value = result.getOrThrow()
            } else {
                Log.e(TAG, "Error fetching profile: ${result.exceptionOrNull()?.message}")
               // SnackbarManager.showSnackbar("Failed to fetch profile data")
            }
        }
    }

    private fun uriToPath(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        contentResolver.query(uri, projection, null, null, null).use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                return cursor.getString(columnIndex)
            }
        }
        throw IllegalArgumentException("Unable to get the file path from URI")
    }


    fun showSuccessPopup(){
        _shouldShowSuccessPopup.value = true
    }

    fun hideSuccessPopup(){
        _shouldShowSuccessPopup.value = false
    }

    fun addImage(images: List<Uri>) {
        _selectedImages.value += images
    }

    fun removeImage(uri: Uri) {
        _selectedImages.value -= uri
    }

    // Clear all selected images
    fun clearImages() {
        _selectedImages.value = emptyList()
    }

    sealed class UploadState {
        object Idle : UploadState()
        object Loading : UploadState()
        data class Success(val response: UploadNewsResponse) : UploadState()
        data class Error(val message: String) : UploadState()
    }

    companion object{
        const val TAG = "NewsApp"
    }
}
