package com.example.sipora.core.util

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

object FileUploadUtil {
    fun createMultipartBody(context: Context, uri: Uri, partName: String): MultipartBody.Part? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileBytes = inputStream?.readBytes()
            inputStream?.close()

            fileBytes?.let {
                val requestFile = it.toRequestBody("application/pdf".toMediaTypeOrNull())
                MultipartBody.Part.createFormData(partName, "file.pdf", requestFile)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
