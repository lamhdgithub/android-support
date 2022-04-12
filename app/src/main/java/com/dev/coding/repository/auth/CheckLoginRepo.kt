package com.dev.coding.repository.auth

import android.support.core.di.Inject
import android.support.core.di.ShareScope
import com.dev.coding.datasource.local.UserLocalSource

@Inject(ShareScope.Fragment)
class CheckLoginRepo(private val userLocalSource: UserLocalSource) {
		operator fun invoke(): Boolean {
				return userLocalSource.getToken().isNotBlank()
		}
}