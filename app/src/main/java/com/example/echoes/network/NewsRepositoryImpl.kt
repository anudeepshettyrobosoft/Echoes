package com.example.echoes.network

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.echoes.mock.mockNewsList
import com.example.echoes.mock.mockProfileData
import com.example.echoes.network.model.NewsItem
import com.example.echoes.network.model.ProfileData
import com.example.echoes.network.model.UploadNewsRequest
import com.example.echoes.network.model.UploadNewsResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val apiService: NewsApiService, // Retrofit or another API service
) : NewsRepository {

    override suspend fun uploadNews(
        newsRequest: UploadNewsRequest,
        contentResolver: ContentResolver,
        context: Context
    ): Result<UploadNewsResponse> {
        return try {
            // Convert fields to RequestBody
            val title = newsRequest.title.toRequestBody("text/plain".toMediaTypeOrNull())
            val description = newsRequest.description.toRequestBody("text/plain".toMediaTypeOrNull())
            val category = newsRequest.category.toRequestBody("text/plain".toMediaTypeOrNull())
            val location = newsRequest.location?.toRequestBody("text/plain".toMediaTypeOrNull())
            val date = newsRequest.reportedDate?.toRequestBody("text/plain".toMediaTypeOrNull())
            val time = newsRequest.reportedTime?.toRequestBody("text/plain".toMediaTypeOrNull())

            // Prepare single image part
            val imageUri = newsRequest.imageList?.firstOrNull() // Get the first image URI
            val imagePart = imageUri?.let { uri ->
                val filePath = uriToPath(uri, contentResolver, context)
                if (filePath != null) {
                    val file = File(filePath)
                    val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"
                    val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("imageORVideo", file.name, requestFile)
                } else {
                    Log.e("UploadNews", "Invalid URI: $uri")
                    null
                }
            }

            if (imagePart == null) {
                return Result.failure(Exception("No valid image provided"))
            }

            // Make the API call
            val response = apiService.uploadNews(
                title,
                description,
                category,
                location,
                date,
                time,
                imagePart
            )

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("API Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("UploadNews", "Error uploading news", e)
            Result.failure(e)
        }
    }

    fun uriToPath(uri: Uri, contentResolver: ContentResolver, context: Context): String? {
        try {
            if (uri.scheme == "content") {
                val projection = arrayOf(MediaStore.Images.Media.DATA)
                contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                        val filePath = cursor.getString(columnIndex)
                        if (!filePath.isNullOrEmpty()) {
                            return filePath
                        }
                    }
                }
                // If `_data` is null or not provided, use fallback
                Log.w(
                    "URI_RESOLVER",
                    "_data column is null or empty for URI: $uri. Using fallback."
                )
            } else if (uri.scheme == "file") {
                return uri.path // Direct file path for file URIs
            }

            // Fallback for unsupported URIs
            val tempFile = uriToTempFile(uri, contentResolver, context)
            return tempFile?.absolutePath
        } catch (e: Exception) {
            Log.e("URI_RESOLVER", "Error resolving URI to path: $uri", e)
            return null
        }
    }

    fun uriToTempFile(uri: Uri, contentResolver: ContentResolver, context: Context): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val tempFile = File(context.cacheDir, "temp_${System.currentTimeMillis()}.jpg")
            tempFile.outputStream().use { output ->
                inputStream?.copyTo(output)
            }
            tempFile
        } catch (e: Exception) {
            Log.e("URI_RESOLVER", "Failed to resolve URI to temp file: $uri", e)
            null
        }
    }

    override suspend fun getNewsList(): Result<List<NewsItem>> {
        //return Result.success(mockNewsList)
          return try {
              val response = apiService.getNewsList()
              if (response.isSuccessful && response.body() != null) {
                  Result.success(response.body()!!)
              } else {
                  Result.failure(Exception(response.message()))
              }
          } catch (e: Exception) {
              Result.failure(e)
          }
    }

    override suspend fun getProfileData(): Result<ProfileData> {
     // return Result.success(mockProfileData)
         return try {
             val response = apiService.getProfileData()
             if (response.isSuccessful && response.body() != null) {
                 Result.success(response.body()!!)
             } else {
                 Result.failure(Exception(response.message()))
             }
         } catch (e: Exception) {
             Result.failure(e)
         }
    }
}
