package com.example.echoes.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echoes.data.model.LoginRequest
import com.example.echoes.data.model.RegisterUserRequest
import com.example.echoes.domain.model.NewsItem
import com.example.echoes.domain.model.ProfileData
import com.example.echoes.data.model.UploadNewsRequest
import com.example.echoes.data.model.UploadNewsResponse
import com.example.echoes.domain.model.Voucher
import com.example.echoes.domain.usecases.GetNewsListUseCase
import com.example.echoes.domain.usecases.GetProfileDataUseCase
import com.example.echoes.domain.usecases.GetVouchersListUseCase
import com.example.echoes.domain.usecases.LoginUseCase
import com.example.echoes.domain.usecases.LogoutUseCase
import com.example.echoes.domain.usecases.RedeemVoucherUseCase
import com.example.echoes.domain.usecases.RegisterUserUseCase
import com.example.echoes.domain.usecases.UploadNewsUseCase
import com.example.echoes.mock.mockVouchers
import com.example.echoes.presentation.utils.SnackbarManager
import com.example.echoes.utils.UserPrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class EchoesViewModel @Inject constructor(
    private val uploadNewsUseCase: UploadNewsUseCase,
    private val getNewsListUseCase: GetNewsListUseCase,
    private val getProfileDataUseCase: GetProfileDataUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getVouchersListUseCase: GetVouchersListUseCase,
    private val redeemVoucherUseCase: RedeemVoucherUseCase
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

    private val _rewardPoints = MutableStateFlow(0)
    val rewardPoints: StateFlow<Int> get() = _rewardPoints

    private val _vouchers = MutableStateFlow<List<Voucher>>(emptyList())
    val vouchers: StateFlow<List<Voucher>> get() = _vouchers

    private val _redeemedVouchers = MutableStateFlow<Set<String>>(emptySet())
    val redeemedVouchers = _redeemedVouchers.asStateFlow()


    var isLoading = mutableStateOf(false)
        private set

    var userId = mutableStateOf<String?>(null)
        private set

    /*init {
        fetchNewsList()
        fetchProfileData()
    }*/



    fun uploadNews(newsRequest: UploadNewsRequest) {
        viewModelScope.launch {
            _uploadState.value = UploadState.Loading
            val result = uploadNewsUseCase(
                userId = userId.value?:"",
                newsRequest.copy(
                imageList = _selectedImages.value.ifEmpty { null }
            ))

            result.onSuccess { response ->
                Log.d(TAG, "Upload success")
                _shouldShowSuccessPopup.value = true
                userId.value?.let { fetchNewsList(userId = it) }
                _uploadState.value = UploadState.Success(response)
            }.onFailure { exception ->
                Log.e(TAG, "Upload failed with exception", exception)
                val errorMessage = exception.message ?: "Something went wrong"
                SnackbarManager.showSnackbar(errorMessage)
                _uploadState.value = UploadState.Error(errorMessage)
            }
        }
    }

    fun fetchNewsList(userId:String) {
        viewModelScope.launch {
            val result = getNewsListUseCase.invoke(userId)
            if (result.isSuccess) {
                _newsListState.value = result.getOrThrow()
            } else {
                Log.e(TAG, "Error fetching news: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun fetchProfileData(onSuccess: (ProfileData) -> Unit = {}) {
        viewModelScope.launch {
            val result = getProfileDataUseCase()
            if (result.isSuccess) {
                val profileResponse = result.getOrThrow()
                _profileState.value = profileResponse
                onSuccess(profileResponse)
            } else {
                Log.e(TAG, "Error fetching profile: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun registerUser(name: String, email: String, phone: String, onResult: (Boolean) -> Unit) {
        setLoadingState(true)
        viewModelScope.launch {
            val result = registerUserUseCase(
                RegisterUserRequest(
                    name = name,
                    email = email,
                    phone = phone
                )
            )

            result.onSuccess {
                setLoadingState(false)
                Log.d(TAG, "Registration success")
                onResult(true)
            }.onFailure { exception ->
                setLoadingState(false)
                Log.e(TAG, "Registration failed with exception", exception)
                val errorMessage = exception.message ?: "Something went wrong"
                SnackbarManager.showSnackbar(errorMessage)
            }
        }
    }

    fun loginUser(context: Context, email: String, password: String, onResult: (Boolean) -> Unit) {
        setLoadingState(true)
        viewModelScope.launch {
            val result = loginUseCase(
                LoginRequest(
                    email = email,
                    password = password
                )
            )

            result.onSuccess {loginResponse->
                setLoadingState(false)
                if(loginResponse.token.isNotEmpty()){
                    Log.d(TAG, "Token: ${loginResponse.token}")
                    UserPrefManager.saveAuthToken(context, loginResponse.token)
                }
                Log.d(TAG, "Login success")
                onResult(true)
            }.onFailure { exception ->
                setLoadingState(false)
                Log.e(TAG, "Login failed with exception", exception)
                val errorMessage = exception.message ?: "Something went wrong"
                SnackbarManager.showSnackbar("Login failed")
            }
        }
    }

    fun logoutUser(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = logoutUseCase()
            if (result.isSuccess) {
                Log.d(TAG, "Logout success")
                onResult(true)
            } else {
                Log.e(TAG, "Logout failed with exception", result.exceptionOrNull())
                val errorMessage = result.exceptionOrNull()?.message ?: "Something went wrong"
                SnackbarManager.showSnackbar("errorMessage")
                onResult(false)
            }
        }
    }

    fun fetchVouchersList() {
        viewModelScope.launch {
            val result = getVouchersListUseCase.invoke()
            if (result.isSuccess) {
                _vouchers.value = result.getOrThrow()
            } else {
                Log.e(TAG, "Error fetching vouchers: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun redeemVoucher(voucherId: String,onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = redeemVoucherUseCase(voucherId)
            if (result.isSuccess) {
                val redeemResponse = result.getOrThrow()
                _rewardPoints.value = redeemResponse.message.toIntOrNull() ?: 0
                markVoucherRedeemed(voucherId)
                Log.d(TAG, "Redemption success")
                onResult(true)
            } else {
                Log.e(TAG, "Redemption failed with exception", result.exceptionOrNull())
                val errorMessage = result.exceptionOrNull()?.message ?: "Something went wrong"
                SnackbarManager.showSnackbar("errorMessage")
                onResult(false)
            }
        }
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

    fun setRewardPoints(points: Int?) {
        points?.let {
            _rewardPoints.value = points
        }

    }

    fun markVoucherRedeemed(voucherId: String) {
        _redeemedVouchers.value += voucherId
    }



    fun setLoadingState(isLoading: Boolean) {
        this.isLoading.value = isLoading
    }

    sealed class UploadState {
        object Idle : UploadState()
        object Loading : UploadState()
        data class Success(val response: UploadNewsResponse) : UploadState()
        data class Error(val message: String) : UploadState()
    }

    sealed class FetchNewsState {
        object Idle : FetchNewsState()
        object Loading : FetchNewsState()
        data class Success(val response: List<NewsItem>) : FetchNewsState()
        data class Error(val message: String) : FetchNewsState()
    }

    companion object{
        const val TAG = "NewsApp"

    }
}
