package com.dev.coding.model.api

class ApiResponse<T>(
		val results: T,
		val info: Info
)

class Info(
		val seed: String? = "",
		val page: Int = 0,
)