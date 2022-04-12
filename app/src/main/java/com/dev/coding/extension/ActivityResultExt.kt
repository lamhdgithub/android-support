package com.dev.coding.extension

import androidx.activity.result.ActivityResultLauncher

fun ActivityResultLauncher<String>.launchImage() {
		this.launch("image/*")
}
