package com.dev.coding.datasource.local

import android.content.Context
import android.support.core.di.Inject
import android.support.core.caching.Caching
import android.support.core.caching.GsonCaching
import android.support.core.caching.Parser
import android.support.core.di.ShareScope
import android.support.core.extensions.withIO
import com.dev.coding.model.entity.AccountEntity
import com.dev.coding.model.request.UserDTO
import com.google.gson.Gson

@Inject(ShareScope.Singleton)
class AppCache(
		private val context: Context,
) : Caching(context, ParserImpl()) {
		
		var email: String by string("email", "")
		var password: String by string("password", "")
		var token: String by string("token", "")
}

class ParserImpl : Parser {
		private val gson = Gson()
		override fun <T> fromJson(string: String?, type: Class<T>): T? {
				return gson.fromJson(string, type)
		}
		
		override fun <T> toJson(value: T?): String {
				return gson.toJson(value)
		}

}