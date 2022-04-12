package com.dev.coding.extension

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

fun Map<String, String>.buildParts(): Map<String, RequestBody> {
		val part = hashMapOf<String, RequestBody>()
		forEach {
				part[it.key] = createTextPart(it.value)
		}
		return part
}

fun createTextPart(value: String): RequestBody {
		return value.toRequestBody("text/plain".toMediaType())
}

fun buildImagePart(name: String, photo: String): MultipartBody.Part {
		val file = File(photo)
		return MultipartBody.Part.createFormData(
				name,
				file.name,
				file.asRequestBody("image/${file.extension}".toMediaType())
		)
}

fun buildPart(name: String, data: Any): MultipartBody.Part {
		return MultipartBody.Part.createFormData(
				name,
				Gson().toJson(data)
		)
}
