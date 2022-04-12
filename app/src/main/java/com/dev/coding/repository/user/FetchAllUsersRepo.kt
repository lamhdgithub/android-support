package com.dev.coding.repository.user

import android.support.core.di.Inject
import android.support.core.di.ShareScope
import android.support.core.livedata.post
import androidx.lifecycle.MutableLiveData
import com.dev.coding.datasource.local.UserLocalSource
import com.dev.coding.datasource.remote.UserApi
import com.dev.coding.factory.UserFactory
import com.dev.coding.model.ui.IUser

@Inject(ShareScope.Fragment)
class FetchAllUsersRepo(
		private val userApi: UserApi,
		private val userLocalSource: UserLocalSource,
		private val userFactory: UserFactory
) {
		val result = MutableLiveData<List<IUser>>()
		
		suspend operator fun invoke() {
				val data = userApi.users().await()
				userLocalSource.saveAll(data)
				
				val users = data.map { userFactory.create(it) }
				result.post(users)
		}
}