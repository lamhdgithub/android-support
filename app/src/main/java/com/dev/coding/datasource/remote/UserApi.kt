package com.dev.coding.datasource.remote

import android.support.core.di.InjectBy
import android.support.core.di.Injectable
import android.support.core.di.ShareScope
import com.dev.coding.helper.network.ApiAsync
import com.dev.coding.helper.network.Async
import com.dev.coding.model.request.UserDTO
import retrofit2.Retrofit
import retrofit2.http.GET

/**
 * https://randomuser.me/api/
 */
@InjectBy(UserApiImpl::class, ShareScope.Singleton)
interface UserApi : Injectable {
		@GET("?results=50")
		fun users(): ApiAsync<List<UserDTO>>
}

class UserApiImpl(private val retrofit: Retrofit) : UserApi by retrofit.create(UserApi::class.java)