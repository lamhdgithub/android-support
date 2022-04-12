package com.dev.coding.datasource.local

import android.content.Context
import android.support.core.caching.GsonCaching
import android.support.core.di.Inject
import android.support.core.di.ShareScope
import android.support.core.extensions.withIO
import com.dev.coding.model.entity.AccountEntity
import com.dev.coding.model.request.UserDTO

@Inject(ShareScope.Singleton)
class UserLocalSource(
		private val context: Context,
		private val appCache: AppCache
) {
		private val caching = GsonCaching(context)
		private var users by caching.reference<List<UserDTO>>("user:list")
		
		suspend fun findByEmail(email: String): UserDTO? {
				return withIO { users?.find { it.email == email } }
		}
		
		suspend fun saveAll(users: List<UserDTO>) {
				withIO { this@UserLocalSource.users = users }
		}
		
		fun getToken(): String {
				return appCache.token
		}
		
		fun saveToken(token: String) {
				appCache.token = token
		}
		
		fun clearToken() {
				appCache.token = ""
		}
		
		fun saveAccount(email: AccountEntity) {
				appCache.email = email.email
				appCache.password = email.password
		}
		
		fun clearAccount() {
				appCache.email = ""
				appCache.password = ""
		}
		
		fun getAccount(): AccountEntity? {
				if (appCache.email.isBlank()) return null
				return AccountEntity(appCache.email, appCache.password)
		}
}