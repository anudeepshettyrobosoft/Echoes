package com.example.echoes.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import coil.request.CachePolicy
import coil.request.ImageRequest
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ImageUtils {
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



    fun getImageRequestWithHeaders(context: Context, imageUrl: String): ImageRequest {
        return ImageRequest.Builder(context)
            .data(imageUrl)
            .addHeader("ngrok-skip-browser-warning", "true")
            .addHeader("User-Agent", "Custom-Agent")
            .diskCachePolicy(CachePolicy.ENABLED) // Optional: Enable disk caching
            .networkCachePolicy(CachePolicy.ENABLED) // Optional: Enable network caching
            .build()
    }
}